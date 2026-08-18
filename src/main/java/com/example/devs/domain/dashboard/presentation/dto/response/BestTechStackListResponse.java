package com.example.devs.domain.dashboard.presentation.dto.response;

import com.example.devs.domain.dashboard.domain.TechTrendPeriod;

import java.util.List;

public record BestTechStackListResponse(
        TechTrendPeriod period,
        Integer majorId,
        List<BestTechStackResponse> techStacks
) {
}
