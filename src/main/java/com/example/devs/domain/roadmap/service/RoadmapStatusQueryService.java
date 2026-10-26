package com.example.devs.domain.roadmap.service;

import com.example.devs.domain.roadmap.domain.Roadmap;
import com.example.devs.domain.roadmap.domain.repository.RoadmapRepository;
import com.example.devs.domain.roadmap.exception.RoadmapNotFoundException;
import com.example.devs.domain.roadmap.presentation.dto.response.RoadmapStatusResponse;
import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoadmapStatusQueryService {

    private final RoadmapRepository roadmapRepository;
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public RoadmapStatusResponse execute(Long roadmapId) {
        Long userId = userFacade.getCurrentUserId();
        Roadmap roadmap = roadmapRepository.findByIdAndUserId(roadmapId, userId)
                .orElseThrow(RoadmapNotFoundException::new);

        return RoadmapStatusResponse.from(roadmap);
    }
}
