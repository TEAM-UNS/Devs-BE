package com.example.devs.domain.company.presentation.dto.response.company_skill_mention;

import java.util.List;

public record SkillMentionListResponse(List<SkillMentionResponse> skills) {
    public static SkillMentionListResponse from(List<SkillMentionResponse> skills) {
        return new SkillMentionListResponse(skills);
    }
}
