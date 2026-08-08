package com.example.devs.domain.market.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.ColumnTransformer;

import java.time.OffsetDateTime;

@Getter
@Entity
@Table(
        name = "company",
        schema = "market",
        uniqueConstraints = @UniqueConstraint(name = "company_name_key_key", columnNames = "name_key"),
        indexes = @Index(name = "company_size_type_idx", columnList = "size_type")
)
@Check(name = "company_employee_count_chk", constraints = "employee_count IS NULL OR employee_count >= 0")
@Check(name = "company_size_type_chk", constraints = "size_type IN ('startup','small','medium','large','enterprise','unknown')")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_key", nullable = false, length = 200)
    private String nameKey;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Column(name = "business_content", columnDefinition = "text")
    private String businessContent;

    @Column(name = "talent_profile", columnDefinition = "text")
    private String talentProfile;

    @Column(name = "size_type", nullable = false, length = 20)
    @ColumnDefault("'unknown'")
    private String sizeType = "unknown";

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(length = 120)
    private String industry;

    @Column(length = 20)
    private String founded;

    private Long revenue;

    @Column(length = 500)
    private String homepage;

    @Column(name = "profile_embedding", columnDefinition = "vector(1024)")
    @ColumnTransformer(write = "cast(? as vector)")
    private String profileEmbedding;

    @Column(name = "embed_hash", length = 64, columnDefinition = "char(64)")
    private String embedHash;

    @Column(name = "raw_fields", nullable = false, columnDefinition = "jsonb")
    @ColumnDefault("'{}'")
    @ColumnTransformer(write = "cast(? as jsonb)")
    private String rawFields = "{}";

    @Column(name = "created_at", nullable = false)
    @ColumnDefault("now()")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @ColumnDefault("now()")
    private OffsetDateTime updatedAt;
}
