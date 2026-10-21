package com.example.devs.domain.user_skill.domain.repository;

import com.example.devs.domain.skill.domain.QSkillField;
import com.example.devs.domain.skill.domain.Skill;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.List;

import static com.example.devs.domain.skill.domain.QSkill.skill;
import static com.example.devs.domain.user_skill.domain.QUserSkill.userSkill;

@RequiredArgsConstructor
public class UserSkillRepositoryCustomImpl implements UserSkillRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public List<Skill> findSkillsByUserId(Long userId) {
        return queryFactory
                .select(skill)
                .from(userSkill)
                .join(userSkill.skill, skill)
                .where(userSkill.user.id.eq(userId))
                .orderBy(skill.name.asc())
                .fetch();
    }

    @Override
    public int deleteAllByUserId(Long userId) {
        entityManager.flush();

        long deletedCount = queryFactory
                .delete(userSkill)
                .where(userSkill.user.id.eq(userId))
                .execute();

        return Math.toIntExact(deletedCount);
    }

    @Override
    public int deleteSkillsNotInMajors(
            Long userId,
            Collection<Integer> majorIds
    ) {
        QSkillField skillField = QSkillField.skillField;

        entityManager.flush();

        long deletedCount = queryFactory
                .delete(userSkill)
                .where(
                        userSkill.user.id.eq(userId),
                        JPAExpressions.selectOne()
                                .from(skillField)
                                .where(
                                        skillField.skill.id.eq(userSkill.skill.id),
                                        skillField.field.id.in(majorIds)
                                )
                                .notExists()
                )
                .execute();

        return Math.toIntExact(deletedCount);
    }
}
