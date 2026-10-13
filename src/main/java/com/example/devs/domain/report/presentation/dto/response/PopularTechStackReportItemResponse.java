package com.example.devs.domain.report.presentation.dto.response;

import com.example.devs.domain.report.domain.Trend;

public record PopularTechStackReportItemResponse(
        int rank,
        Integer techStackId,
        String name,
        long searchCount,
        long previousSearchCount,
        long changeCount,
        Double changeRate,
        Trend trend
) {
    public static PopularTechStackReportItemResponse of(
            int rank,
            Integer techStackId,
            String name,
            long searchCount,
            long previousSearchCount,
            long changeCount,
            Double changeRate,
            Trend trend
    ) {
        return new PopularTechStackReportItemResponse(
                rank,
                techStackId,
                name,
                searchCount,
                previousSearchCount,
                changeCount,
                changeRate,
                trend
        );
    }
}
