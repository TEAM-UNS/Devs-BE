package com.example.devs.domain.company.presentation.dto.response.company_top_skill;

import com.example.devs.domain.company.domain.Company;
import com.example.devs.domain.company.presentation.dto.response.company_skill.SkillRankResponse;

import java.util.List;

public record CompanyTopSkillResponse(Long companyId, String name, List<SkillRankResponse> skills) {
    public static CompanyTopSkillResponse of(Company company, List<SkillRankResponse> skills) {
        return new CompanyTopSkillResponse(company.getId(), company.getName(), skills);
    }
}
