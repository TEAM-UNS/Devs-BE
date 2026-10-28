package com.example.devs.domain.roadmap.presentation.dto.response;

import com.example.devs.domain.roadmap.domain.Roadmap;
import com.example.devs.domain.roadmap.domain.RoadmapDayCompletion;
import com.example.devs.domain.roadmap.domain.RoadmapItem;
import lombok.Builder;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public record RoadmapResponse(
        Long roadmapId,
        String status,
        String fieldName,
        String summary,
        int learningRate,
        List<RoadmapDayResponse> days
) {
    public static RoadmapResponse of(
            Roadmap roadmap, List<RoadmapItem> items, List<RoadmapDayCompletion> completions, int openedDayNo
    ) {
        Map<Integer, RoadmapDayCompletion> completionByDayNo = completions.stream()
                .collect(Collectors.toMap(RoadmapDayCompletion::getDayNo, completion -> completion));

        long doneCount = items.stream().filter(RoadmapItem::isDone).count();
        int learningRate = items.isEmpty() ? 0 : (int) (doneCount * 100 / items.size());

        List<RoadmapDayResponse> days = items.stream()
                .collect(Collectors.groupingBy(RoadmapItem::getDayNo, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .map(entry -> RoadmapDayResponse.of(
                        entry.getKey(),
                        entry.getValue(),
                        entry.getKey() > openedDayNo,
                        completionByDayNo.get(entry.getKey())
                ))
                .toList();

        return RoadmapResponse.builder()
                .roadmapId(roadmap.getId())
                .status(roadmap.getStatus().databaseValue())
                .fieldName(roadmap.getField() != null ? roadmap.getField().getName() : null)
                .summary(roadmap.getOutline() != null
                        && roadmap.getOutline().get("summary") instanceof String summary ? summary : null)
                .learningRate(learningRate)
                .days(days)
                .build();
    }
}
