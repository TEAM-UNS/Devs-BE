package com.example.devs.domain.dashboard.presentation.dto.response;

import java.util.List;

public record CompanySizeTechStackListResponse(
        String companySize,
        String category,
        List<CompanySizeTechStackResponse> techStacks
) {
}
