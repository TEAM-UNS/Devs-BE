package com.example.devs.domain.roadmap.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Getter
@Entity
@Table(
        name = "roadmap_day_completion",
        schema = "market",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_roadmap_day_completion",
                columnNames = {"roadmap_id", "day_no"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoadmapDayCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roadmap_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Roadmap roadmap;

    @JdbcTypeCode(SqlTypes.SMALLINT)
    @Column(name = "day_no", nullable = false)
    private int dayNo;

    @Column(columnDefinition = "text")
    private String retrospect;

    @CreationTimestamp
    @Column(name = "completed_at", nullable = false, updatable = false)
    private OffsetDateTime completedAt;

    @Builder
    public RoadmapDayCompletion(Roadmap roadmap, int dayNo, String retrospect) {
        this.roadmap = roadmap;
        this.dayNo = dayNo;
        this.retrospect = retrospect;
    }
}
