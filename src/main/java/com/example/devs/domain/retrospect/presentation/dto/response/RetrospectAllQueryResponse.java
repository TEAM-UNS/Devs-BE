package com.example.devs.domain.retrospect.presentation.dto.response;

import com.example.devs.domain.retrospect.domain.Retrospect;
import lombok.Builder;

import java.time.LocalDate;
import java.time.ZoneId;

@Builder
public record RetrospectAllQueryResponse(
        Long retrospectId,
        int dayNo,
        String content,
        LocalDate date
) {
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    public static RetrospectAllQueryResponse from(Retrospect retrospect) {
        return RetrospectAllQueryResponse.builder()
                .retrospectId(retrospect.getId())
                .dayNo(retrospect.getDayNo())
                .content(retrospect.getContent())
                .date(retrospect.getCreatedAt().atZoneSameInstant(SEOUL).toLocalDate())
                .build();
    }
}
