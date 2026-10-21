package com.example.devs.domain.user_major.domain.repository;

import com.example.devs.domain.user_major.domain.UserMajor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMajorRepository extends
        JpaRepository<UserMajor, Long>,
        UserMajorRepositoryCustom {
    boolean existsByUserId(Long userId);
}
