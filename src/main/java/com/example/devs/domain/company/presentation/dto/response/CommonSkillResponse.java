package com.example.devs.domain.company.presentation.dto.response;

public record CommonSkillResponse(String skillName, int companyCount, int totalCompanyCount) {
    public static CommonSkillResponse of(String skillName, int companyCount, int totalCompanyCount) {
        return new CommonSkillResponse(skillName, companyCount, totalCompanyCount);
    }
}
