package com.example.devs.domain.dashboard.presentation.dto.response;

public record PopularTechStackResponse(
        int rank,
        Integer techStackId,
        String name,
        long count
) {
    public static PopularTechStackResponse of(
            int rank,
            Integer techStackId,
            String name,
            long count
    ) {
        return new PopularTechStackResponse(
                rank,
                techStackId,
                name,
                count
        );
    }
}
