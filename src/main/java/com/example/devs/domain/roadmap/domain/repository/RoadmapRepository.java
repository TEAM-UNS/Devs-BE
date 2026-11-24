package com.example.devs.domain.roadmap.domain.repository;

import com.example.devs.domain.roadmap.domain.Roadmap;
import com.example.devs.domain.roadmap.domain.RoadmapStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {
    @EntityGraph(attributePaths = "field")
    List<Roadmap> findByUserIdOrderByCreatedAtDescIdDesc(Long userId);

    Optional<Roadmap> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserIdAndStatusIn(Long userId, Collection<RoadmapStatus> statuses);

    long countByUserId(Long userId);

    @Modifying(clearAutomatically = true)
    @Query("""
            update Roadmap r
            set r.status = :failedStatus, r.errorMessage = :errorMessage
            where r.status in :inProgressStatuses
              and r.createdAt < :createdBefore
            """)
    int failInProgressCreatedBefore(
            @Param("inProgressStatuses") Collection<RoadmapStatus> inProgressStatuses,
            @Param("createdBefore") OffsetDateTime createdBefore,
            @Param("failedStatus") RoadmapStatus failedStatus,
            @Param("errorMessage") String errorMessage
    );
}
