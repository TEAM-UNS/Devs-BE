package com.example.devs.domain.roadmap.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Getter
@Entity
@DynamicUpdate
@Table(
        name = "roadmap_item",
        schema = "market",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_roadmap_item",
                columnNames = {"roadmap_id", "day_no", "seq"}
        ),
        indexes = {
                @Index(name = "idx_roadmap_item_day", columnList = "roadmap_id,day_no,seq"),
                @Index(name = "idx_roadmap_item_week", columnList = "roadmap_id,week_no")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoadmapItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "roadmap_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Roadmap roadmap;

    @JdbcTypeCode(SqlTypes.SMALLINT)
    @Column(name = "week_no", nullable = false)
    private int weekNo;

    @JdbcTypeCode(SqlTypes.SMALLINT)
    @Column(name = "day_no", nullable = false)
    private int dayNo;

    @JdbcTypeCode(SqlTypes.SMALLINT)
    @Column(nullable = false)
    private int seq;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "is_done", nullable = false)
    @ColumnDefault("false")
    private boolean done;

    @Column(name = "done_at")
    private OffsetDateTime doneAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public void updateDone(boolean done) {
        if (this.done == done) {
            return;
        }
        this.done = done;
        this.doneAt = done ? OffsetDateTime.now() : null;
    }
}
