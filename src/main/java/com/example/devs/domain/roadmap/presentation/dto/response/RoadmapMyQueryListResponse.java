package com.example.devs.domain.roadmap.presentation.dto.response;

import com.example.devs.domain.roadmap.domain.Roadmap;
import com.example.devs.domain.tech_field.domain.TechField;
import lombok.Builder;

import java.time.LocalDate;
import java.time.ZoneId;

@Builder
public record RoadmapMyQueryListResponse(
        Long roadmapId,
        Integer majorId,
        String majorName,
        int targetWeeks,
        String status,
        LocalDate date
) {
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    public static RoadmapMyQueryListResponse from(Roadmap roadmap) {
        TechField field = roadmap.getField();

        return RoadmapMyQueryListResponse.builder()
                .roadmapId(roadmap.getId())
                .majorId(field != null ? field.getId() : null)
                .majorName(field != null ? field.getName() : null)
                .targetWeeks(roadmap.getTargetWeeks())
                .status(roadmap.getStatus().databaseValue())
                .date(roadmap.getCreatedAt().atZoneSameInstant(SEOUL).toLocalDate())
                .build();
    }
}
