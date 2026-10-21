package com.example.devs.domain.company.presentation.dto.response.company_skill_mention;

import java.util.List;

public record SkillMentionResponse(String skillName, List<CompanyMentionCountResponse> counts) {
    public static SkillMentionResponse of(String skillName, List<CompanyMentionCountResponse> counts) {
        return new SkillMentionResponse(skillName, counts);
    }
}
