package com.example.devs.domain.roadmap.presentation.dto.response;

import com.example.devs.domain.roadmap.domain.RoadmapItem;
import lombok.Builder;

@Builder
public record RoadmapItemResponse(
        Long itemId,
        String title,
        String description,
        boolean done
) {
    public static RoadmapItemResponse from(RoadmapItem item) {
        return RoadmapItemResponse.builder()
                .itemId(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .done(item.isDone())
                .build();
    }
}
