package com.example.devs.domain.roadmap.service;

import com.example.devs.domain.roadmap.domain.repository.RoadmapRepository;
import com.example.devs.domain.roadmap.presentation.dto.response.RoadmapMyQueryListResponse;
import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoadmapMyQueryListService {

    private final RoadmapRepository roadmapRepository;
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public List<RoadmapMyQueryListResponse> execute() {
        Long userId = userFacade.getCurrentUserId();
        return roadmapRepository.findByUserIdOrderByCreatedAtDescIdDesc(userId).stream()
                .map(RoadmapMyQueryListResponse::from)
                .toList();
    }
}
