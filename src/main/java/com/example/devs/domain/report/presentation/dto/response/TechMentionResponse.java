package com.example.devs.domain.report.presentation.dto.response;

public record TechMentionResponse(
        String name,
        long previous,
        long current
) {
    public static TechMentionResponse of(String name, long previous, long current) {
        return new TechMentionResponse(name, previous, current);
    }
}
