package com.example.devs.domain.dashboard.presentation.dto.response;

import java.util.List;

public record PopularTechStackListResponse(
        String major,
        List<PopularTechStackResponse> techStacks
) {
}
