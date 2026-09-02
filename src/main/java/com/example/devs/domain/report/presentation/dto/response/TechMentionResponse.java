package com.example.devs.domain.report.presentation.dto.response;

public record TechMentionResponse(
        String name,
        long previous,
        long current
) {
}
