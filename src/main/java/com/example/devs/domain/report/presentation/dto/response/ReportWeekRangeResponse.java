package com.example.devs.domain.report.presentation.dto.response;

import com.example.devs.domain.report.domain.ReportWeek;

import java.time.LocalDate;

public record ReportWeekRangeResponse(
        LocalDate startDate,
        LocalDate endDate
) {
    public static ReportWeekRangeResponse from(ReportWeek week) {
        return new ReportWeekRangeResponse(
                week.currentStart().toLocalDate(),
                week.currentEnd().toLocalDate().minusDays(1)
        );
    }
}
