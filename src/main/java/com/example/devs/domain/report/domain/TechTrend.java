package com.example.devs.domain.report.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record TechTrend(
        Integer skillId,
        String skillName,
        long previousCount,
        long currentCount,
        double changeRate
) {
    // @JsonIgnore: 리포트 jsonb 컬럼에 increase/decrease 필드가 함께 저장되지 않게 한다.

    /** 지난주 0건이던 신규 기술은 증가율을 계산할 수 없으므로 증가 후보에서 뺀다. */
    @JsonIgnore
    public boolean isIncrease() {
        return previousCount > 0 && changeRate > 0;
    }

    @JsonIgnore
    public boolean isDecrease() {
        return changeRate < 0;
    }
}
