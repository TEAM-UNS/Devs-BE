package com.example.devs.domain.roadmap.domain.repository;

import com.example.devs.domain.roadmap.domain.Roadmap;
import com.example.devs.domain.roadmap.domain.RoadmapStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    @EntityGraph(attributePaths = "field")
    List<Roadmap> findByUserIdOrderByCreatedAtDescIdDesc(Long userId);

    Optional<Roadmap> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserIdAndStatusIn(Long userId, Collection<RoadmapStatus> statuses);

    long countByUserId(Long userId);
}
