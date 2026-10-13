package com.example.devs.domain.dashboard.presentation.dto.response;

public record DashboardSummaryResponse(
        long todayCollectedCount,
        long todayDiff,
        long activeCompanyCount,
        long companyDiff,
        MentionedTechResponse mostMentionedTech,
        RisingTechResponse mostRisingTech
) {
    public static DashboardSummaryResponse of(
            long todayCollectedCount,
            long todayDiff,
            long activeCompanyCount,
            long companyDiff,
            MentionedTechResponse mostMentionedTech,
            RisingTechResponse mostRisingTech
    ) {
        return new DashboardSummaryResponse(
                todayCollectedCount,
                todayDiff,
                activeCompanyCount,
                companyDiff,
                mostMentionedTech,
                mostRisingTech
        );
    }
}
