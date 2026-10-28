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
import com.example.devs.domain.roadmap.exception.RoadmapNotFoundException;
import com.example.devs.domain.roadmap.presentation.dto.response.RoadmapResponse;
import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadmapMyQueryService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final RoadmapRepository roadmapRepository;
    private final RoadmapItemRepository roadmapItemRepository;
    private final RoadmapDayCompletionRepository roadmapDayCompletionRepository;
    private final RetrospectRepository retrospectRepository;
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public RoadmapResponse execute() {
        Long userId = userFacade.getCurrentUserId();
        Roadmap roadmap = roadmapRepository.findByUserId(userId)
                .orElseThrow(RoadmapNotFoundException::new);

        if (roadmap.getStatus() != RoadmapStatus.COMPLETED) {
            return RoadmapResponse.of(roadmap, List.of(), List.of(), List.of(), 0);
        }

        List<RoadmapItem> items = roadmapItemRepository.findByRoadmapIdOrderByDayNoAscSeqAsc(roadmap.getId());
        List<RoadmapDayCompletion> completions = roadmapDayCompletionRepository.findByRoadmapId(roadmap.getId());
        List<Retrospect> retrospects = retrospectRepository.findByDayCompletionRoadmapId(roadmap.getId());

        return RoadmapResponse.of(
                roadmap, items, completions, retrospects, roadmap.openedDayNo(ZonedDateTime.now(SEOUL))
        );
    }
}
