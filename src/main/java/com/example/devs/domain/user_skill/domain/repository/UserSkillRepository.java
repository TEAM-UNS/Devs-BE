package com.example.devs.domain.user_skill.domain.repository;

import com.example.devs.domain.user_skill.domain.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    @Modifying(flushAutomatically = true)
    @Query("delete from UserSkill userSkill where userSkill.user.id = :userId")
    int deleteAllByUserId(@Param("userId") Long userId);

    @Modifying(flushAutomatically = true)
    @Query("""
            delete from UserSkill userSkill
            where userSkill.user.id = :userId
              and not exists (
                  select skillField.id
                  from SkillField skillField
                  where skillField.skill.id = userSkill.skill.id
                    and skillField.field.id in :majorIds
              )
            """)
    int deleteSkillsNotInMajors(
            @Param("userId") Long userId,
            @Param("majorIds") Collection<Integer> majorIds
    );
}
