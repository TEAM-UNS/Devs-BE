package com.example.devs.domain.report.presentation.dto.response;

import com.example.devs.domain.report.domain.TechTrend;

public record TechTrendResponse(
        Integer skillId,
        String skillName,
        double changeRate
) {
    /** 변화율은 소수 첫째 자리까지 반올림한다. */
    public static TechTrendResponse from(TechTrend trend) {
        return new TechTrendResponse(
                trend.skillId(),
                trend.skillName(),
                Math.round(trend.changeRate() * 10) / 10.0
        );
    }
}
