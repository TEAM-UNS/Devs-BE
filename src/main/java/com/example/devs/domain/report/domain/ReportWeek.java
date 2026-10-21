package com.example.devs.domain.report.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

/** 서울 시간 기준 주간 조회 범위. 시작은 포함하고 종료는 제외한다. */
public record ReportWeek(
        OffsetDateTime previousStart,
        OffsetDateTime currentStart,
        OffsetDateTime currentEnd
) {
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    public static ReportWeek from(LocalDate baseDate) {
        OffsetDateTime start = baseDate
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay(SEOUL)
                .toOffsetDateTime();
        return new ReportWeek(start.minusWeeks(1), start, start.plusWeeks(1));
    }
}
