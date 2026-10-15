package com.example.devs.domain.company.presentation.dto.response.company_skill;

public record SkillRankResponse(int rank, String skillName, int percentage) {
    public static SkillRankResponse of(int rank, String skillName, int percentage) {
        return new SkillRankResponse(rank, skillName, percentage);
    }
}