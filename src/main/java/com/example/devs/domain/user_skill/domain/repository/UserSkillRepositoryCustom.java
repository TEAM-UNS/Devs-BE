package com.example.devs.domain.user_skill.domain.repository;

import java.util.Collection;

public interface UserSkillRepositoryCustom {
    int deleteAllByUserId(Long userId);

    int deleteSkillsNotInMajors(Long userId, Collection<Integer> majorIds);
}
