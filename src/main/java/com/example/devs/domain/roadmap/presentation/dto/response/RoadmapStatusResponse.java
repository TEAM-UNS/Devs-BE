package com.example.devs.domain.roadmap.presentation.dto.response;

import com.example.devs.domain.roadmap.domain.Roadmap;
import com.example.devs.domain.roadmap.domain.RoadmapStatus;
import lombok.Builder;

@Builder
public record RoadmapStatusResponse(
        Long roadmapId,
        String status,
        int generatedWeeks,
        int targetWeeks,
        int progress,
        String errorMessage
) {
    public static RoadmapStatusResponse from(Roadmap roadmap) {
        int progress = roadmap.getStatus() == RoadmapStatus.COMPLETED
                ? 100
                : roadmap.getGeneratedWeeks() * 100 / roadmap.getTargetWeeks();

        return RoadmapStatusResponse.builder()
                .roadmapId(roadmap.getId())
                .status(roadmap.getStatus().databaseValue())
                .generatedWeeks(roadmap.getGeneratedWeeks())
                .targetWeeks(roadmap.getTargetWeeks())
                .progress(progress)
                .errorMessage(roadmap.getErrorMessage())
                .build();
    }
}
