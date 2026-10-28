package com.example.devs.domain.retrospect.domain;

import com.example.devs.domain.roadmap.domain.RoadmapDayCompletion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "retrospect", schema = "market")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Retrospect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_completion_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private RoadmapDayCompletion dayCompletion;

    @JdbcTypeCode(SqlTypes.SMALLINT)
    @Column(name = "day_no", nullable = false)
    private int dayNo;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Builder
    public Retrospect(Long userId, RoadmapDayCompletion dayCompletion, int dayNo, String content) {
        this.userId = userId;
        this.dayCompletion = dayCompletion;
        this.dayNo = dayNo;
        this.content = content;
    }
}
