package com.example.devs.domain.user_major.domain;

import com.example.devs.domain.major.domain.Major;
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
        name = "tbl_user_major",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_major",
                columnNames = {"user_id", "major_id"}
        )
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserMajor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_major_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "major_id", nullable = false)
    private Major major;

    @Builder
    public UserMajor(User user, Major major) {
        this.user = user;
        this.major = major;
    }
}
