package com.example.devs.domain.skill.domain.repository;

import com.example.devs.domain.skill.domain.SkillField;

import java.util.Collection;
import java.util.List;

public interface SkillFieldRepositoryCustom {
    long countMatchedSkills(
            Collection<Integer> skillIds,
            Collection<Integer> fieldIds
    );

    List<SkillField> findAllWithSkillByFieldIds(Collection<Integer> fieldIds);
}
