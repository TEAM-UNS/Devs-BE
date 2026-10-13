package com.example.devs.domain.company.presentation.dto.response;

import java.util.List;

public record CompanyTopSkillListResponse(List<CompanyTopSkillResponse> companies) {
    public static CompanyTopSkillListResponse from(List<CompanyTopSkillResponse> companies) {
        return new CompanyTopSkillListResponse(companies);
    }
}
