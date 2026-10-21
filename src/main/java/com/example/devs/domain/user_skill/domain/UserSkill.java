package com.example.devs.domain.user_skill.domain;

import com.example.devs.domain.skill.domain.Skill;
import com.example.devs.domain.user.domain.User;
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

@Getter
@Entity
@Table(
        name = "tbl_user_skill",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_skill",
                columnNames = {"user_id", "skill_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSkill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_skill_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Builder
    public UserSkill(User user, Skill skill) {
        this.user = user;
        this.skill = skill;
    }
}
