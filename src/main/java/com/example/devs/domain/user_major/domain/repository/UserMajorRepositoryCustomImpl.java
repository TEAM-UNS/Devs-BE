package com.example.devs.domain.user_major.domain.repository;

import com.example.devs.domain.tech_field.domain.TechField;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.devs.domain.user_major.domain.QUserMajor.userMajor;
import static com.example.devs.domain.tech_field.domain.QTechField.techField;

@RequiredArgsConstructor
public class UserMajorRepositoryCustomImpl implements UserMajorRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public List<TechField> findMajorsByUserId(Long userId) {
        return queryFactory
                .select(techField)
                .from(userMajor)
                .join(userMajor.major, techField)
                .where(userMajor.user.id.eq(userId))
                .orderBy(techField.sortOrder.asc(), techField.id.asc())
                .fetch();
    }

    @Override
    public List<Integer> findMajorIdsByUserId(Long userId) {
        return queryFactory
                .select(userMajor.major.id)
                .from(userMajor)
                .where(userMajor.user.id.eq(userId))
                .fetch();
    }

    @Override
    public int deleteAllByUserId(Long userId) {
        entityManager.flush();

        long deletedCount = queryFactory
                .delete(userMajor)
                .where(userMajor.user.id.eq(userId))
                .execute();

        return Math.toIntExact(deletedCount);
    }
}
