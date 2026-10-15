package com.example.devs.domain.company.presentation.dto.response.company_posting;

import com.example.devs.domain.company.domain.Company;

public record CompanyPostingCountResponse(
        Long companyId,
        String name,
        long postingCount
) {
    public static CompanyPostingCountResponse of(Company company, long postingCount) {
        return new CompanyPostingCountResponse(company.getId(), company.getName(), postingCount);
    }
}