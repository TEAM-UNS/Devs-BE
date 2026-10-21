package com.example.devs.domain.skill.domain;

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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(
        name = "skill_alias",
        schema = "market",
        uniqueConstraints = @UniqueConstraint(name = "skill_alias_alias_key", columnNames = "alias"),
        indexes = @Index(name = "skill_alias_skill_idx", columnList = "skill_id")
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SkillAlias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Skill skill;

    @Column(nullable = false, length = 120)
    private String alias;

    @Column(name = "case_sensitive", nullable = false)
    @ColumnDefault("false")
    private boolean caseSensitive;
}
