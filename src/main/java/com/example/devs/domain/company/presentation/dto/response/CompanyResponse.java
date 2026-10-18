package com.example.devs.domain.company.presentation.dto.response;

import com.example.devs.domain.company.domain.Company;
import lombok.Builder;

@Builder
public record CompanyResponse(
        Long id,
        String name,
        String description,
        String logoUrl
) {
    public static CompanyResponse from(Company company) {
        return CompanyResponse.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .logoUrl(company.getLogoUrl())
                .build();
    }
}
