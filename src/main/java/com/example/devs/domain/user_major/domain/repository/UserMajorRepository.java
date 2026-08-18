package com.example.devs.domain.user_major.domain.repository;

import com.example.devs.domain.user_major.domain.UserMajor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserMajorRepository extends JpaRepository<UserMajor, Long> {
    @Modifying(flushAutomatically = true)
    @Query("delete from UserMajor userMajor where userMajor.user.id = :userId")
    int deleteAllByUserId(@Param("userId") Long userId);
}
