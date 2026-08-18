package com.example.devs.domain.dashboard.presentation.dto.response;

public record DashboardSummaryResponse(
        long todayCollectedCount,
        long todayDiff,
        long activeCompanyCount,
        long companyDiff,
        MentionedTechResponse mostMentionedTech,
        RisingTechResponse mostRisingTech
) {
}
