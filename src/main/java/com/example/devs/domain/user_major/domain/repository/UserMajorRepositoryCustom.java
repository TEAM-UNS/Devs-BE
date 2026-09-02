package com.example.devs.domain.user_major.domain.repository;

import java.util.List;

public interface UserMajorRepositoryCustom {
    List<Integer> findMajorIdsByUserId(Long userId);

    int deleteAllByUserId(Long userId);
}
