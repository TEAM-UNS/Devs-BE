package com.example.devs.domain.tech_field.presentation.dto.response;

import com.example.devs.domain.skill.domain.Skill;

public record TechStackResponse(
        Integer id,
        String name
) {
    public static TechStackResponse from(Skill skill) {
        return new TechStackResponse(skill.getId(), skill.getName());
    }
}
