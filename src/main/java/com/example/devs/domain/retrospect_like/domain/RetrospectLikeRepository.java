package com.example.devs.domain.retrospect_like.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RetrospectLikeRepository extends JpaRepository<RetrospectLike, Long> {
    boolean existsByRetrospectIdAndUserId(Long retrospectId, Long userId);

    Optional<RetrospectLike> findByRetrospectIdAndUserId(Long retrospectId, Long userId);

    long countByRetrospectId(Long retrospectId);

    @Query("""
            select new com.example.devs.domain.retrospect_like.domain.RetrospectLikeCount(
                    retrospectLike.retrospect.id, count(retrospectLike)
            )
            from RetrospectLike retrospectLike
            where retrospectLike.retrospect.id in :retrospectIds
            group by retrospectLike.retrospect.id
            """)
    List<RetrospectLikeCount> countByRetrospectIds(@Param("retrospectIds") List<Long> retrospectIds);

    @Query("""
            select retrospectLike.retrospect.id
            from RetrospectLike retrospectLike
            where retrospectLike.user.id = :userId
              and retrospectLike.retrospect.id in :retrospectIds
            """)
    List<Long> findLikedRetrospectIds(
            @Param("userId") Long userId,
            @Param("retrospectIds") List<Long> retrospectIds
    );
}
