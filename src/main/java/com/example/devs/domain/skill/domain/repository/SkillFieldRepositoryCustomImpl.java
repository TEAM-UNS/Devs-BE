package com.example.devs.domain.skill.domain.repository;

import com.example.devs.domain.skill.domain.QSkill;
import com.example.devs.domain.skill.domain.SkillField;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

import static com.example.devs.domain.skill.domain.QSkillField.skillField;

@RequiredArgsConstructor
public class SkillFieldRepositoryCustomImpl implements SkillFieldRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public long countMatchedSkills(
            Collection<Integer> skillIds,
            Collection<Integer> fieldIds
    ) {
        Long matchedCount = queryFactory
                .select(skillField.skill.id.countDistinct())
                .from(skillField)
                .where(
                        skillField.skill.id.in(skillIds),
                        skillField.field.id.in(fieldIds)
                )
                .fetchOne();

        return matchedCount == null ? 0L : matchedCount;
    }

    @Override
    public List<SkillField> findAllWithSkillByFieldIds(
            Collection<Integer> fieldIds
    ) {
        QSkill skill = QSkill.skill;

        return queryFactory
                .selectFrom(skillField)
                .join(skillField.skill, skill)
                .fetchJoin()
                .where(skillField.field.id.in(fieldIds))
                .orderBy(skillField.field.sortOrder.asc(), skill.name.asc())
                .fetch();
    }
}
