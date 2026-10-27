package com.example.devs.domain.roadmap.service;

import com.example.devs.domain.roadmap.domain.Roadmap;
import com.example.devs.domain.roadmap.domain.RoadmapItem;
import com.example.devs.domain.roadmap.domain.RoadmapStatus;
import com.example.devs.domain.roadmap.domain.repository.RoadmapDayCompletionRepository;
import com.example.devs.domain.roadmap.domain.repository.RoadmapItemRepository;
import com.example.devs.domain.roadmap.exception.RoadmapDayAlreadyCompletedException;
import com.example.devs.domain.roadmap.exception.RoadmapDayLockedException;
import com.example.devs.domain.roadmap.exception.RoadmapItemNotFoundException;
import com.example.devs.domain.roadmap.exception.RoadmapNotReadyException;
import com.example.devs.domain.roadmap.presentation.dto.request.RoadmapItemCheckRequest;
import com.example.devs.domain.roadmap.presentation.dto.response.RoadmapItemCheckResponse;
import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class RoadmapItemCheckService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final RoadmapItemRepository roadmapItemRepository;
    private final RoadmapDayCompletionRepository roadmapDayCompletionRepository;
    private final UserFacade userFacade;

    @Transactional
    public RoadmapItemCheckResponse execute(Long itemId, RoadmapItemCheckRequest request) {
        Long userId = userFacade.getCurrentUserId();
        RoadmapItem item = roadmapItemRepository.findByIdAndRoadmapUserId(itemId, userId)
                .orElseThrow(RoadmapItemNotFoundException::new);
        Roadmap roadmap = item.getRoadmap();

        if (roadmap.getStatus() != RoadmapStatus.COMPLETED) {
            throw new RoadmapNotReadyException();
        }

        if (item.getDayNo() > roadmap.openedDayNo(ZonedDateTime.now(SEOUL))) {
            throw new RoadmapDayLockedException();
        }

        if (roadmapDayCompletionRepository.existsByRoadmapIdAndDayNo(roadmap.getId(), item.getDayNo())) {
            throw new RoadmapDayAlreadyCompletedException();
        }

        item.updateDone(request.done());

        boolean dayAllItemsDone = roadmapItemRepository.findByRoadmapIdAndDayNo(roadmap.getId(), item.getDayNo())
                .stream()
                .allMatch(RoadmapItem::isDone);
        long totalCount = roadmapItemRepository.countByRoadmapId(roadmap.getId());
        long doneCount = roadmapItemRepository.countByRoadmapIdAndDoneTrue(roadmap.getId());

        return RoadmapItemCheckResponse.builder()
                .itemId(item.getId())
                .done(item.isDone())
                .dayAllItemsDone(dayAllItemsDone)
                .learningRate((int) (doneCount * 100 / totalCount))
                .build();
    }
}
