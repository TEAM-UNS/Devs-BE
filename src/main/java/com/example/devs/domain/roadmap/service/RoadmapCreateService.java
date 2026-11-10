package com.example.devs.domain.roadmap.service;

import com.example.devs.domain.roadmap.domain.Roadmap;
import com.example.devs.domain.roadmap.domain.RoadmapStatus;
import com.example.devs.domain.roadmap.domain.repository.RoadmapRepository;
import com.example.devs.domain.roadmap.exception.RoadmapAlreadyGeneratingException;
import com.example.devs.domain.roadmap.exception.RoadmapDailyLimitExceededException;
import com.example.devs.domain.roadmap.exception.RoadmapGenerationRequestFailedException;
import com.example.devs.domain.roadmap.exception.RoadmapLimitExceededException;
import com.example.devs.domain.roadmap.presentation.dto.request.RoadmapCreateRequest;
import com.example.devs.domain.roadmap.presentation.dto.response.RoadmapCreateResponse;
import com.example.devs.domain.skill.domain.repository.SkillRepository;
import com.example.devs.domain.skill.exception.SkillNotFoundException;
import com.example.devs.domain.tech_field.domain.TechField;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.exception.MajorNotFoundException;
import com.example.devs.domain.user.facade.UserFacade;
import com.example.devs.global.error.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadmapCreateService {
    private static final Duration TIMEOUT = Duration.ofSeconds(10);
    private static final List<RoadmapStatus> IN_PROGRESS_STATUSES =
            List.of(RoadmapStatus.PENDING, RoadmapStatus.GENERATING);
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final int MAX_ROADMAP_COUNT = 10;
    private static final int DAILY_CREATE_LIMIT = 3;
    private static final String DAILY_CREATE_KEY_PREFIX = "roadmap:daily-create:";
    private static final Duration DAILY_CREATE_EXPIRATION = Duration.ofDays(1);

    private final UserFacade userFacade;
    private final TechFieldRepository techFieldRepository;
    private final SkillRepository skillRepository;
    private final RoadmapRepository roadmapRepository;
    private final WebClient aiWebClient;
    private final StringRedisTemplate redisTemplate;

    public RoadmapCreateResponse execute(RoadmapCreateRequest request) {
        Long userId = userFacade.getCurrentUserId();

        TechField field = techFieldRepository.findById(request.majorId())
                .orElseThrow(MajorNotFoundException::new);

        if (skillRepository.findAllById(request.skillIds()).size() != request.skillIds().size()) {
            throw new SkillNotFoundException();
        }

        if (roadmapRepository.existsByUserIdAndStatusIn(userId, IN_PROGRESS_STATUSES)) {
            throw new RoadmapAlreadyGeneratingException();
        }

        if (roadmapRepository.countByUserId(userId) >= MAX_ROADMAP_COUNT) {
            throw new RoadmapLimitExceededException();
        }

        String dailyCreateKey = DAILY_CREATE_KEY_PREFIX + userId + ":"
                + LocalDate.now(SEOUL).format(DateTimeFormatter.BASIC_ISO_DATE);
        String dailyCreateCount = redisTemplate.opsForValue().get(dailyCreateKey);
        if (dailyCreateCount != null && Integer.parseInt(dailyCreateCount) >= DAILY_CREATE_LIMIT) {
            throw new RoadmapDailyLimitExceededException();
        }

        Roadmap roadmap;
        try {
            roadmap = roadmapRepository.save(Roadmap.builder()
                    .userId(userId)
                    .field(field)
                    .targetWeeks(request.targetWeeks())
                    .skillIds(new ArrayList<>(request.skillIds()))
                    .build());
        } catch (DataIntegrityViolationException e) {
            throw new RoadmapAlreadyGeneratingException();
        }

        try {
            aiWebClient.post()
                    .uri("/api/roadmaps/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new AiRoadmapRequest(
                            roadmap.getId(),
                            userId,
                            request.majorId(),
                            request.skillIds(),
                            request.targetWeeks(),
                            request.additionalComment()
                    ))
                    .retrieve()
                    .toBodilessEntity()
                    .block(TIMEOUT);
        } catch (Exception e) {
            log.error("로드맵 생성 요청 실패. userId={}, roadmapId={}", userId, roadmap.getId(), e);
            roadmap.fail(ErrorCode.ROADMAP_GENERATION_REQUEST_FAILED.getMessage());
            roadmapRepository.save(roadmap);
            throw new RoadmapGenerationRequestFailedException();
        }

        redisTemplate.opsForValue().increment(dailyCreateKey);
        redisTemplate.expire(dailyCreateKey, DAILY_CREATE_EXPIRATION);

        return RoadmapCreateResponse.from(roadmap);
    }

    private record AiRoadmapRequest(
            @JsonProperty("roadmap_id") Long roadmapId,
            @JsonProperty("user_id") Long userId,
            @JsonProperty("field_id") Integer fieldId,
            @JsonProperty("skill_ids") Set<Integer> skillIds,
            @JsonProperty("target_weeks") Integer targetWeeks,
            @JsonProperty("additional_comment") String additionalComment
    ) {}
}
