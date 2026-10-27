package com.example.devs.domain.roadmap.presentation.dto.response;

import com.example.devs.domain.roadmap.domain.RoadmapItem;
import lombok.Builder;

import java.util.List;

@Builder
public record RoadmapDayResponse(
        int dayNo,
        int weekNo,
        boolean completed,
        List<RoadmapItemResponse> items
) {
    public static RoadmapDayResponse of(int dayNo, List<RoadmapItem> items) {
        return RoadmapDayResponse.builder()
                .dayNo(dayNo)
                .weekNo(items.get(0).getWeekNo())
                .completed(items.stream().allMatch(RoadmapItem::isDone))
                .items(items.stream().map(RoadmapItemResponse::from).toList())
                .build();
    }
}
