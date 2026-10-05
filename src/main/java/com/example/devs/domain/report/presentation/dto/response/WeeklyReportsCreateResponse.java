package com.example.devs.domain.report.presentation.dto.response;

import java.time.LocalDate;

public record WeeklyReportsCreateResponse(
        LocalDate weekStartDate,
        int createdCount,
        int skippedCount,
        int failedCount
) {
}
