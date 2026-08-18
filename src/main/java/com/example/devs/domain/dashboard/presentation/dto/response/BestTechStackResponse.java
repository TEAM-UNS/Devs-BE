package com.example.devs.domain.dashboard.presentation.dto.response;

import java.util.List;

public record BestTechStackResponse(
        Integer techStackId,
        String name,
        List<TechStackTrendValueResponse> values
) {
}
