package com.example.devs.domain.roadmap.service;

import com.example.devs.domain.roadmap.domain.Roadmap;
import com.example.devs.domain.roadmap.domain.RoadmapItem;
import com.example.devs.domain.roadmap.domain.RoadmapStatus;
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
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public RoadmapResponse execute() {
        Long userId = userFacade.getCurrentUserId();
        Roadmap roadmap = roadmapRepository.findByUserId(userId)
                .orElseThrow(RoadmapNotFoundException::new);

        List<RoadmapItem> items = roadmap.getStatus() == RoadmapStatus.COMPLETED
                ? roadmapItemRepository.findByRoadmapIdOrderByDayNoAscSeqAsc(roadmap.getId())
                : List.of();

        return RoadmapResponse.of(roadmap, items, roadmap.openedDayNo(ZonedDateTime.now(SEOUL)));
    }
}
