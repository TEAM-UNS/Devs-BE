package com.example.devs.domain.user.domain.repository;

import com.example.devs.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    @Query("select u.email from User u where u.totalReportSubscribed = true")
    List<String> findTotalReportSubscriberEmails();

    @Query("""
        select new com.example.devs.domain.user.domain.repository.MajorReportSubscriberProjection(m, u.email)
        from User u
        join u.reportMajorIds m
        where u.majorReportSubscribed = true
        """)
    List<MajorReportSubscriberProjection> findMajorReportSubscribers();
}
