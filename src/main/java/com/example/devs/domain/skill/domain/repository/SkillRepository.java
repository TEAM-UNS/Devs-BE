package com.example.devs.domain.skill.domain.repository;

import com.example.devs.domain.skill.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
}
