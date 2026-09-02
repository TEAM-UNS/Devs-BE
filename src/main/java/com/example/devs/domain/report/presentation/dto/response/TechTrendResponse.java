package com.example.devs.domain.report.presentation.dto.response;

public record   TechTrendResponse(
        Integer skillId,
        String skillName,
        double changeRate
) {
}
