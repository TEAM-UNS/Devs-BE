package com.example.devs.domain.skill.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.ColumnTransformer;

import java.time.OffsetDateTime;

@Getter
@Entity
@Table(
        name = "skill",
        schema = "market",
        uniqueConstraints = @UniqueConstraint(name = "skill_name_key", columnNames = "name")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(length = 32)
    private String category;

    @Column(name = "is_ambiguous", nullable = false)
    @ColumnDefault("false")
    private boolean ambiguous;

    @Column(columnDefinition = "vector(1024)")
    @ColumnTransformer(write = "cast(? as vector)")
    private String embedding;

    @Column(name = "created_at", nullable = false)
    @ColumnDefault("now()")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @ColumnDefault("now()")
    private OffsetDateTime updatedAt;

    @Column(name = "is_common", nullable = false)
    @ColumnDefault("false")
    private boolean common;
}
