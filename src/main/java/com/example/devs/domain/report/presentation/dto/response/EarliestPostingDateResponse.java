package com.example.devs.domain.report.presentation.dto.response;

import java.time.LocalDate;

public record EarliestPostingDateResponse(LocalDate earliestPostingDate) {
    public static EarliestPostingDateResponse from(LocalDate earliestPostingDate) {
        return new EarliestPostingDateResponse(earliestPostingDate);
    }
}
