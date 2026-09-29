package com.example.devs.domain.report.domain;

import java.time.LocalDate;
import java.util.List;

public record WeeklyReportStatistics(
        long weeklyCollectedPostingCount,
        LocalDate earliestPostingDate,
        List<PopularTechStack> popularTechStacks,
        List<TechMention> techMentions,
        TechTrend maxIncreaseTech,
        TechTrend maxDecreaseTech
) {
    public WeeklyReportStatistics {
        if (weeklyCollectedPostingCount < 0) {
            throw new IllegalArgumentException("weeklyCollectedPostingCount must not be negative");
        }
        popularTechStacks = List.copyOf(popularTechStacks);
        techMentions = List.copyOf(techMentions);
    }

    public record PopularTechStack(
            int rank, Integer techStackId, String name,
            long searchCount, long previousSearchCount, long changeCount,
            Double changeRate, Trend trend
    ) {
    }

    public record TechMention(String name, long previous, long current) {
    }
}
