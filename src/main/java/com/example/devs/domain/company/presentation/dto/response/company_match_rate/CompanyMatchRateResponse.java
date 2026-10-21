package com.example.devs.domain.company.presentation.dto.response.company_match_rate;

import com.example.devs.domain.company.domain.Company;

public record CompanyMatchRateResponse(
        Long companyId,
        String name,
        int matchRate
) {
    public static CompanyMatchRateResponse of(Company company, int matchRate) {
        return new CompanyMatchRateResponse(company.getId(), company.getName(), matchRate);
    }
}
