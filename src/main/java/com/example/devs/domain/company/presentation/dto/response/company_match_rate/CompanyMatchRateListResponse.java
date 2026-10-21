package com.example.devs.domain.company.presentation.dto.response.company_match_rate;

import java.util.List;

public record CompanyMatchRateListResponse(List<CompanyMatchRateResponse> companies) {
    public static CompanyMatchRateListResponse from(List<CompanyMatchRateResponse> companies) {
        return new CompanyMatchRateListResponse(companies);
    }
}
