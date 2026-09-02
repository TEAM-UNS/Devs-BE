package com.example.devs.domain.report.domain.repository;

import com.example.devs.domain.job_posting.domain.QJobPosting;
import com.example.devs.domain.job_posting.domain.QPostingSkill;
import com.example.devs.domain.skill.domain.QSkill;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PostingSkillRepositoryCustomImpl implements PostingSkillRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<SkillCountProjection> findSkillCountsByPeriod(
            Integer majorId,
            OffsetDateTime start,
            OffsetDateTime end
    ) {
        QPostingSkill postingSkill = QPostingSkill.postingSkill;
        QJobPosting posting = QJobPosting.jobPosting;
        QSkill skill = QSkill.skill;

        BooleanExpression majorCondition = majorId == null
                ? null
                : posting.field.id.eq(majorId);
        NumberExpression<Long> mentionCount = posting.id.countDistinct();

        List<Tuple> rows = queryFactory
                .select(skill.id, skill.name, mentionCount)
                .from(postingSkill)
                .join(postingSkill.posting, posting)
                .join(postingSkill.skill, skill)
                .where(
                        majorCondition,
                        posting.postedAt.goe(start),
                        posting.postedAt.lt(end)
                )
                .groupBy(skill.id, skill.name)
                .fetch();

        return rows.stream()
                .map(row -> new SkillCountProjection(
                        row.get(skill.id),
                        row.get(skill.name),
                        row.get(mentionCount)
                ))
                .toList();
    }
}
