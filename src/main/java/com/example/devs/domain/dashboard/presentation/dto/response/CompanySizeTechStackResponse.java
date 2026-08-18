package com.example.devs.domain.dashboard.presentation.dto.response;

public record CompanySizeTechStackResponse(
        int rank,
        String name,
        int percentage
) {
}
