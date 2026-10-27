package com.example.devs.domain.roadmap.domain.repository;

import com.example.devs.domain.roadmap.domain.RoadmapItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapItemRepository extends JpaRepository<RoadmapItem, Long> {
    List<RoadmapItem> findByRoadmapIdOrderByDayNoAscSeqAsc(Long roadmapId);
}
