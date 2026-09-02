package com.example.devs.domain.report.domain.repository;

public record SkillCountProjection(
        Integer skillId,
        String skillName,
        Long mentionCount
) {
}
