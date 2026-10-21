package com.example.devs.domain.skill.domain.repository;

import com.example.devs.domain.skill.domain.SkillField;
import com.example.devs.domain.skill.domain.SkillFieldId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillFieldRepository extends
        JpaRepository<SkillField, SkillFieldId>,
        SkillFieldRepositoryCustom {
}
