package com.example.devs.domain.company.presentation.dto.response.company_skill;

public record CommonSkillResponse(String skillName, int companyCount, int totalCompanyCount) {
    public static CommonSkillResponse of(String skillName, int companyCount, int totalCompanyCount) {
        return new CommonSkillResponse(skillName, companyCount, totalCompanyCount);
    }
}
