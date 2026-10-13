package com.example.devs.domain.report.presentation.dto.response;

public record WeeklyCollectedPostingCountResponse(
        long count
) {
    public static WeeklyCollectedPostingCountResponse from(long count) {
        return new WeeklyCollectedPostingCountResponse(count);
    }
}
