package com.example.devs.domain.roadmap.service;

import com.example.devs.domain.company.domain.repository.CompanyRepository;
import com.example.devs.domain.company.exception.CompanyNotFoundException;
import com.example.devs.domain.roadmap.exception.RoadmapGenerationRequestFailedException;
import com.example.devs.domain.roadmap.presentation.dto.request.RoadmapCreateRequest;
import com.example.devs.domain.skill.domain.repository.SkillRepository;
import com.example.devs.domain.skill.exception.SkillNotFoundException;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.exception.MajorNotFoundException;
import com.example.devs.domain.user.facade.UserFacade;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadmapCreateService {
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final UserFacade userFacade;
    private final TechFieldRepository techFieldRepository;
    private final CompanyRepository companyRepository;
    private final SkillRepository skillRepository;
    private final WebClient aiWebClient;

    public void execute(RoadmapCreateRequest request) {
        Long userId = userFacade.getCurrentUserId();

        if (!techFieldRepository.existsById(request.majorId())) {
            throw new MajorNotFoundException();
        }

        if (companyRepository.findAllById(request.companyIds()).size() != request.companyIds().size()) {
            throw new CompanyNotFoundException();
        }

        if (skillRepository.findAllById(request.skillIds()).size() != request.skillIds().size()) {
            throw new SkillNotFoundException();
        }

        try {
            aiWebClient.post()
                    .uri("/api/roadmaps/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new AiRoadmapRequest(
                            userId,
                            request.majorId(),
                            request.companyIds(),
                            request.skillIds(),
                            request.targetWeeks()
                    ))
                    .retrieve()
                    .toBodilessEntity()
                    .block(TIMEOUT);
        } catch (Exception e) {
            log.error("로드맵 생성 요청 실패. userId={}", userId, e);
            throw new RoadmapGenerationRequestFailedException();
        }
    }

    private record AiRoadmapRequest(
            @JsonProperty("user_id") Long userId,
            @JsonProperty("field_id") Integer fieldId,
            @JsonProperty("company_ids") Set<Long> companyIds,
            @JsonProperty("skill_ids") Set<Integer> skillIds,
            @JsonProperty("target_weeks") Integer targetWeeks
    ) {}
}
