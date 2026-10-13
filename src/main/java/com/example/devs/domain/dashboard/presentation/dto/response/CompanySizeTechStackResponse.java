package com.example.devs.domain.dashboard.presentation.dto.response;

public record CompanySizeTechStackResponse(
        int rank,
        String name,
        int percentage
) {
    public static CompanySizeTechStackResponse of(int rank, String name, int percentage) {
        return new CompanySizeTechStackResponse(rank, name, percentage);
    }
}
