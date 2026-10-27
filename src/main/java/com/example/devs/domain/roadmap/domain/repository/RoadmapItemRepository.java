package com.example.devs.domain.roadmap.domain.repository;

import com.example.devs.domain.roadmap.domain.RoadmapItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoadmapItemRepository extends JpaRepository<RoadmapItem, Long> {
    List<RoadmapItem> findByRoadmapIdOrderByDayNoAscSeqAsc(Long roadmapId);

    List<RoadmapItem> findByRoadmapIdAndDayNo(Long roadmapId, int dayNo);

    Optional<RoadmapItem> findByIdAndRoadmapUserId(Long id, Long userId);

    long countByRoadmapId(Long roadmapId);

    long countByRoadmapIdAndDoneTrue(Long roadmapId);
}
