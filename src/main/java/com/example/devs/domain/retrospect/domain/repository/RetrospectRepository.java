package com.example.devs.domain.retrospect.domain.repository;

import com.example.devs.domain.retrospect.domain.Retrospect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface RetrospectRepository extends JpaRepository<Retrospect, Long> {
    List<Retrospect> findByDayCompletionRoadmapId(Long roadmapId);

    List<Retrospect> findByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(OffsetDateTime createdAt);
}
