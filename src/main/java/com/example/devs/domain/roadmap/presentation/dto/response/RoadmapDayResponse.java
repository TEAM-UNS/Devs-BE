package com.example.devs.domain.roadmap.presentation.dto.response;

import com.example.devs.domain.roadmap.domain.RoadmapItem;
import lombok.Builder;

import java.util.List;

@Builder
public record RoadmapDayResponse(
        int dayNo,
        int weekNo,
        boolean locked,
        boolean completed,
        List<RoadmapItemResponse> items
) {
    public static RoadmapDayResponse of(int dayNo, List<RoadmapItem> items, boolean locked) {
        return RoadmapDayResponse.builder()
                .dayNo(dayNo)
                .weekNo(items.get(0).getWeekNo())
                .locked(locked)
                .completed(!locked && items.stream().allMatch(RoadmapItem::isDone))
                .items(items.stream()
                        .map(locked ? RoadmapItemResponse::locked : RoadmapItemResponse::from)
                        .toList())
                .build();
    }
}
