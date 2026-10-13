package com.example.devs.domain.report.presentation.dto.response;

import java.time.LocalDate;

public record WeeklyReportsCreateResponse(
        LocalDate weekStartDate,
        int createdCount,
        int skippedCount,
        int failedCount,
        boolean generationRequested
) {
    public static WeeklyReportsCreateResponse of(
            LocalDate weekStartDate,
            int createdCount,
            int skippedCount,
            int failedCount,
            boolean generationRequested
    ) {
        return new WeeklyReportsCreateResponse(
                weekStartDate,
                createdCount,
                skippedCount,
                failedCount,
                generationRequested
        );
    }
}
