package com.example.devs.domain.user_major.domain.repository;

import com.example.devs.domain.tech_field.domain.TechField;

import java.util.List;

public interface UserMajorRepositoryCustom {
    List<TechField> findMajorsByUserId(Long userId);

    List<Integer> findMajorIdsByUserId(Long userId);

    int deleteAllByUserId(Long userId);
}
