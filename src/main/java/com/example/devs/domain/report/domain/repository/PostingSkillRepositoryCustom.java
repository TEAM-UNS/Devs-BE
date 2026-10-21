package com.example.devs.domain.report.domain.repository;

import java.time.OffsetDateTime;
import java.util.List;

public interface PostingSkillRepositoryCustom {
    List<SkillCountProjection> findSkillCountsByPeriod(
            Integer majorId,
            OffsetDateTime start,
            OffsetDateTime end
    );
}
