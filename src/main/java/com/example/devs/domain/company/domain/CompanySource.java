package com.example.devs.domain.company.domain;

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
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Entity
@Table(
        name = "company_source",
        schema = "market",
        uniqueConstraints = @UniqueConstraint(
                name = "company_source_uk",
                columnNames = {"source", "source_company_id"}
        ),
        indexes = @Index(name = "company_source_company_idx", columnList = "company_id")
)
@Check(name = "company_source_source_chk", constraints = "source IN ('saramin','jobkorea','wanted','jumpit')")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanySource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Company company;

    @Column(nullable = false, length = 20)
    private String source;

    @Column(name = "source_company_id", nullable = false, length = 100)
    private String sourceCompanyId;

    @Column(length = 500)
    private String url;

    @Column(name = "collected_at", nullable = false)
    @ColumnDefault("now()")
    private OffsetDateTime collectedAt;
}
