package com.example.devs.domain.roadmap.presentation.dto.response;

import com.example.devs.domain.roadmap.domain.Roadmap;
import lombok.Builder;

@Builder
public record RoadmapCreateResponse(
        Long roadmapId,
        String status
) {
    public static RoadmapCreateResponse from(Roadmap roadmap) {
        return RoadmapCreateResponse.builder()
                .roadmapId(roadmap.getId())
                .status(roadmap.getStatus().databaseValue())
                .build();
    }
}
