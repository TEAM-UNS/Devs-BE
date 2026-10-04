package com.example.devs.domain.user.presentation.dto.response;

import com.example.devs.domain.skill.domain.Skill;

public record UserTechStackResponse(
        Integer skillId,
        String skillName
) {
    public static UserTechStackResponse from(Skill skill) {
        return new UserTechStackResponse(skill.getId(), skill.getName());
    }
}
