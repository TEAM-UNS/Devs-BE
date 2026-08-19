package com.example.devs.domain.user_major.domain.repository;

import com.example.devs.domain.user_major.domain.UserMajor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserMajorRepository extends JpaRepository<UserMajor, Long> {
    boolean existsByUserId(Long userId);

    @Query("select userMajor.major.id from UserMajor userMajor where userMajor.user.id = :userId")
    List<Integer> findMajorIdsByUserId(@Param("userId") Long userId);

    @Modifying(flushAutomatically = true)
    @Query("delete from UserMajor userMajor where userMajor.user.id = :userId")
    int deleteAllByUserId(@Param("userId") Long userId);
}
