package com.example.devs.domain.dashboard.presentation.dto.response;

import java.time.LocalDate;

public record TechStackTrendValueResponse(
        LocalDate date,
        long value
) {
    public static TechStackTrendValueResponse of(LocalDate date, long value) {
        return new TechStackTrendValueResponse(date, value);
    }
}
