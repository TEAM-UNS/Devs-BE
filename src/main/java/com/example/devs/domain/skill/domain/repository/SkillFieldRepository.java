package com.example.devs.domain.skill.domain.repository;

import com.example.devs.domain.skill.domain.SkillField;
import com.example.devs.domain.skill.domain.SkillFieldId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface SkillFieldRepository extends JpaRepository<SkillField, SkillFieldId> {
    @Query("""
            select count(distinct sf.skill.id)
            from SkillField sf
            where sf.skill.id in :skillIds
              and sf.field.id in :fieldIds
            """)
    long countMatchedSkills(
            @Param("skillIds") Collection<Integer> skillIds,
            @Param("fieldIds") Collection<Integer> fieldIds
    );
}
