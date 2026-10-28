package com.example.devs.domain.roadmap.domain.repository;

import com.example.devs.domain.roadmap.domain.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    Optional<Roadmap> findByUserId(Long userId);

    Optional<Roadmap> findByIdAndUserId(Long id, Long userId);
}
