package com.example.devs.domain.report.presentation.dto.response;

import com.example.devs.domain.report.domain.ReportWeek;

import java.time.LocalDate;

public record ReportWeekRangeResponse(
        int year,
        int month,
        int week,
        LocalDate startDate,
        LocalDate endDate
) {
    public static ReportWeekRangeResponse from(ReportWeek week) {
        LocalDate startDate = week.currentStart().toLocalDate();
        LocalDate thursday = startDate.plusDays(3);
        return new ReportWeekRangeResponse(
                thursday.getYear(),
                thursday.getMonthValue(),
                (thursday.getDayOfMonth() - 1) / 7 + 1,
                startDate,
                week.currentEnd().toLocalDate().minusDays(1)
        );
    }
}
