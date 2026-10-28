package com.example.devs.domain.roadmap.presentation.dto.response;

import lombok.Builder;

@Builder
public record RoadmapItemCheckResponse(
        Long itemId,
        boolean done,
        boolean dayAllItemsDone,
        int learningRate
) {
}
