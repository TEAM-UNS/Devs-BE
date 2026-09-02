package com.example.devs.domain.report.domain.repository;

import com.example.devs.domain.job_posting.domain.PostingSkill;
import com.example.devs.domain.job_posting.domain.PostingSkillId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingSkillRepository extends
        JpaRepository<PostingSkill, PostingSkillId>,
        PostingSkillRepositoryCustom {
}
