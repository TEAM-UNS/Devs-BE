package com.example.devs.domain.report.domain.repository;

public interface SkillCountProjection {
    Integer getSkillId();

    String getSkillName();

    Long getMentionCount();
}
