package com.example.devs.domain.user_skill.domain.repository;

import com.example.devs.domain.skill.domain.Skill;

import java.util.Collection;
import java.util.List;

public interface UserSkillRepositoryCustom {
    List<Skill> findSkillsByUserId(Long userId);

    int deleteAllByUserId(Long userId);

    int deleteSkillsNotInMajors(Long userId, Collection<Integer> majorIds);
}
