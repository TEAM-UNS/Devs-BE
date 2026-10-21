package com.example.devs.domain.user.domain;

import com.example.devs.domain.user.exception.InvalidOAuthProfileException;
import com.example.devs.domain.user.util.EmailNormalizer;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "tbl_user")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PersonalHistory personalHistory;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean totalReportSubscribed;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean majorReportSubscribed;

    @Builder.Default
    @ElementCollection
    @CollectionTable(
            name = "tbl_report_subscription",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "field_id", nullable = false)
    private Set<Integer> reportMajorIds = new HashSet<>();

    public void updateReportSubscription(
            boolean totalReportSubscribed,
            boolean majorReportSubscribed,
            Set<Integer> reportMajorIds
    ) {
        this.totalReportSubscribed = totalReportSubscribed;
        this.majorReportSubscribed = majorReportSubscribed;
        this.reportMajorIds.clear();
        this.reportMajorIds.addAll(reportMajorIds);
    }

    public void updatePersonalHistory(PersonalHistory personalHistory) {
        this.personalHistory = personalHistory;
    }

    public static String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new InvalidOAuthProfileException();
        }
        return EmailNormalizer.normalize(email);
    }

    public static String resolveName(String name, String email) {
        if (name != null && !name.isBlank()) {
            return name;
        }
        return email.substring(0, email.indexOf('@'));
    }
}
