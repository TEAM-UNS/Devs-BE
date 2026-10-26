package com.example.devs.domain.roadmap.domain;

import com.example.devs.domain.tech_field.domain.TechField;
import jakarta.persistence.CheckConstraint;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Entity
@DynamicUpdate
@Table(
        name = "roadmap",
        schema = "market",
        indexes = @Index(name = "uk_roadmap_user", columnList = "user_id", unique = true),
        check = @CheckConstraint(
                name = "roadmap_target_weeks_check",
                constraint = "target_weeks BETWEEN 1 AND 12"
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Roadmap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private TechField field;

    @JdbcTypeCode(SqlTypes.SMALLINT)
    @Column(name = "target_weeks", nullable = false)
    private int targetWeeks;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "skill_ids", nullable = false, columnDefinition = "jsonb")
    @ColumnDefault("'[]'::jsonb")
    private List<Integer> skillIds = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> outline;

    @Convert(converter = RoadmapStatusConverter.class)
    @Column(nullable = false, length = 20)
    @ColumnDefault("'pending'")
    private RoadmapStatus status = RoadmapStatus.PENDING;

    @JdbcTypeCode(SqlTypes.SMALLINT)
    @Column(name = "generated_weeks", nullable = false)
    @ColumnDefault("0")
    private int generatedWeeks;

    @Column(name = "error_message", columnDefinition = "text")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Builder
    public Roadmap(Long userId, TechField field, int targetWeeks, List<Integer> skillIds) {
        this.userId = userId;
        this.field = field;
        this.targetWeeks = targetWeeks;
        this.skillIds = skillIds;
    }

    public boolean isInProgress() {
        return status == RoadmapStatus.PENDING || status == RoadmapStatus.GENERATING;
    }

    public void fail(String errorMessage) {
        this.status = RoadmapStatus.FAILED;
        this.errorMessage = errorMessage;
    }
}
