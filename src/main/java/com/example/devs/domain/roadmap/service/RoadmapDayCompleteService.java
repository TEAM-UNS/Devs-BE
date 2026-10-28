package com.example.devs.domain.roadmap.service;

import com.example.devs.domain.retrospect.domain.Retrospect;
import com.example.devs.domain.retrospect.domain.repository.RetrospectRepository;
import com.example.devs.domain.roadmap.domain.Roadmap;
import com.example.devs.domain.roadmap.domain.RoadmapDayCompletion;
import com.example.devs.domain.roadmap.domain.RoadmapItem;
import com.example.devs.domain.roadmap.domain.RoadmapStatus;
import com.example.devs.domain.roadmap.domain.repository.RoadmapDayCompletionRepository;
import com.example.devs.domain.roadmap.domain.repository.RoadmapItemRepository;
import com.example.devs.domain.roadmap.domain.repository.RoadmapRepository;
import com.example.devs.domain.roadmap.exception.RoadmapDayAlreadyCompletedException;
import com.example.devs.domain.roadmap.exception.RoadmapDayItemsNotDoneException;
import com.example.devs.domain.roadmap.exception.RoadmapDayLockedException;
import com.example.devs.domain.roadmap.exception.RoadmapDayNotFoundException;
import com.example.devs.domain.roadmap.exception.RoadmapNotFoundException;
import com.example.devs.domain.roadmap.exception.RoadmapNotReadyException;
import com.example.devs.domain.roadmap.presentation.dto.request.RoadmapRetrospectRequest;
import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadmapDayCompleteService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final RoadmapRepository roadmapRepository;
    private final RoadmapItemRepository roadmapItemRepository;
    private final RoadmapDayCompletionRepository roadmapDayCompletionRepository;
    private final RetrospectRepository retrospectRepository;
    private final UserFacade userFacade;

    @Transactional
    public void execute(int dayNo, RoadmapRetrospectRequest request) {
        Long userId = userFacade.getCurrentUserId();
        Roadmap roadmap = roadmapRepository.findByUserId(userId)
                .orElseThrow(RoadmapNotFoundException::new);

        if (roadmap.getStatus() != RoadmapStatus.COMPLETED) {
            throw new RoadmapNotReadyException();
        }

        if (dayNo > roadmap.openedDayNo(ZonedDateTime.now(SEOUL))) {
            throw new RoadmapDayLockedException();
        }

        List<RoadmapItem> items = roadmapItemRepository.findByRoadmapIdAndDayNo(roadmap.getId(), dayNo);
        if (items.isEmpty()) {
            throw new RoadmapDayNotFoundException();
        }

        if (!items.stream().allMatch(RoadmapItem::isDone)) {
            throw new RoadmapDayItemsNotDoneException();
        }

        if (roadmapDayCompletionRepository.existsByRoadmapIdAndDayNo(roadmap.getId(), dayNo)) {
            throw new RoadmapDayAlreadyCompletedException();
        }

        RoadmapDayCompletion completion;
        try {
            completion = roadmapDayCompletionRepository.save(RoadmapDayCompletion.builder()
                    .roadmap(roadmap)
                    .dayNo(dayNo)
                    .build());
        } catch (DataIntegrityViolationException e) {
            throw new RoadmapDayAlreadyCompletedException();
        }

        if (StringUtils.hasText(request.retrospect())) {
            retrospectRepository.save(Retrospect.builder()
                    .userId(userId)
                    .dayCompletion(completion)
                    .dayNo(dayNo)
                    .content(request.retrospect().strip())
                    .build());
        }
    }
}
