package com.example.devs.domain.user_tech_stack.domain;

import com.example.devs.domain.tech_stack.domain.TechStack;
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
        name = "tbl_user_tech_stack",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_tech_stack",
                columnNames = {"user_id", "tech_stack_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserTechStack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_tech_stack_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tech_stack_id", nullable = false)
    private TechStack techStack;

    @Builder
    public UserTechStack(User user, TechStack techStack) {
        this.user = user;
        this.techStack = techStack;
    }
}
