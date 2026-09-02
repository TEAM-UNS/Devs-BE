package com.example.devs.domain.report.domain;

public record TechTrend(
        Integer skillId,
        String skillName,
        long previousCount,
        long currentCount,
        double changeRate
) {
}
