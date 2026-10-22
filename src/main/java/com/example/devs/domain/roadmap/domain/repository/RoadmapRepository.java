package com.example.devs.domain.roadmap.domain.repository;

import com.example.devs.domain.roadmap.domain.Roadmap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
}
