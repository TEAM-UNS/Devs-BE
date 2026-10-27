package com.example.devs.domain.roadmap.presentation.dto.response;

import com.example.devs.domain.roadmap.domain.RoadmapDayCompletion;
import com.example.devs.domain.roadmap.domain.RoadmapItem;
import lombok.Builder;

import java.util.List;

@Builder
public record RoadmapDayResponse(
        int dayNo,
        int weekNo,
        boolean locked,
        boolean allItemsDone,
        boolean completed,
        String retrospect,
        List<RoadmapItemResponse> items
) {
    public static RoadmapDayResponse of(
            int dayNo, List<RoadmapItem> items, boolean locked, RoadmapDayCompletion completion
    ) {
        return RoadmapDayResponse.builder()
                .dayNo(dayNo)
                .weekNo(items.get(0).getWeekNo())
                .locked(locked)
                .allItemsDone(!locked && items.stream().allMatch(RoadmapItem::isDone))
                .completed(completion != null)
                .retrospect(completion != null ? completion.getRetrospect() : null)
                .items(items.stream()
                        .map(locked ? RoadmapItemResponse::locked : RoadmapItemResponse::from)
                        .toList())
                .build();
    }
}
