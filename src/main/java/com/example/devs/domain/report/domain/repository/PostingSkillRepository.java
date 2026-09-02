package com.example.devs.domain.report.domain.repository;

import com.example.devs.domain.job_posting.domain.PostingSkill;
import com.example.devs.domain.job_posting.domain.PostingSkillId;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;

public interface PostingSkillRepository extends JpaRepository<PostingSkill, PostingSkillId> {
    
    @Query("""
        SELECT
            ps.skill.id AS skillId,
            ps.skill.name AS skillName,
            COUNT(DISTINCT ps.posting.id) AS mentionCount
        FROM PostingSkill ps
        WHERE ps.posting.postedAt >= :start
          AND ps.posting.postedAt < :end
        GROUP BY ps.skill.id, ps.skill.name
    """)
    List<SkillCountProjection> findSkillCountsByPeriod(
            @Param("start") OffsetDateTime start,
            @Param("end") OffsetDateTime end
    );
}