package com.example.devs.domain.company.presentation.dto.response;

import java.util.List;

public record CompanyPostingCountListResponse(
        List<CompanyPostingCountResponse> companies
) {
    public static CompanyPostingCountListResponse from(List<CompanyPostingCountResponse> companies) {
        return new CompanyPostingCountListResponse(companies);
    }
}