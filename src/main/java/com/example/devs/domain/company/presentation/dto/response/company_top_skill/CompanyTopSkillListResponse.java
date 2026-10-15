package com.example.devs.domain.company.presentation.dto.response.company_top_skill;

import java.util.List;

public record CompanyTopSkillListResponse(List<CompanyTopSkillResponse> companies) {
    public static CompanyTopSkillListResponse from(List<CompanyTopSkillResponse> companies) {
        return new CompanyTopSkillListResponse(companies);
    }
}
