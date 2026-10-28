package com.example.devs.domain.roadmap.domain.repository;

import com.example.devs.domain.roadmap.domain.RoadmapDayCompletion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapDayCompletionRepository extends JpaRepository<RoadmapDayCompletion, Long> {
    List<RoadmapDayCompletion> findByRoadmapId(Long roadmapId);

    boolean existsByRoadmapIdAndDayNo(Long roadmapId, int dayNo);
}
