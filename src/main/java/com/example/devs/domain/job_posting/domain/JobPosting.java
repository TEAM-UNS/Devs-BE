package com.example.devs.domain.job_posting.domain;

import com.example.devs.domain.company.domain.Company;
import com.example.devs.domain.tech_field.domain.TechField;

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
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Getter
@Entity
@Table(
        name = "job_posting",
        schema = "market",
        uniqueConstraints = @UniqueConstraint(
                name = "job_posting_uk",
                columnNames = {"source", "source_job_id"}
        ),
        indexes = {
                @Index(name = "job_posting_career_idx", columnList = "career_min,career_max"),
                @Index(name = "job_posting_collected_idx", columnList = "collected_at"),
                @Index(name = "job_posting_company_idx", columnList = "company_id"),
                @Index(name = "job_posting_field_posted_idx", columnList = "field_id,posted_at"),
                @Index(name = "job_posting_location_idx", columnList = "location"),
                @Index(name = "job_posting_posted_idx", columnList = "posted_at")
        }
)
@Check(name = "job_posting_source_chk", constraints = "source IN ('saramin','jobkorea','wanted','jumpit')")
@Check(name = "job_posting_career_chk", constraints = "career_min IS NULL OR career_max IS NULL OR career_min <= career_max")
@Check(name = "job_posting_salary_chk", constraints = "salary_min IS NULL OR salary_max IS NULL OR salary_min <= salary_max")
@Check(name = "job_posting_salary_type_chk", constraints = "salary_type IN ('range','min_only','max_only','negotiable','unknown')")
@Check(name = "job_posting_salary_period_chk", constraints = "salary_period IS NULL OR salary_period IN ('annual','monthly','hourly')")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String source;

    @Column(name = "source_job_id", nullable = false, length = 100)
    private String sourceJobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private TechField field;

    @Column(nullable = false, length = 300)
    private String title;

    @Column(name = "company_name_raw", length = 200)
    private String companyNameRaw;

    @Column(name = "tags_raw", nullable = false, columnDefinition = "jsonb")
    @ColumnDefault("'[]'")
    @ColumnTransformer(write = "cast(? as jsonb)")
    private String tagsRaw = "[]";

    @Column(name = "career_min")
    private Integer careerMin;

    @Column(name = "career_max")
    private Integer careerMax;

    @Column(name = "employment_type", length = 30)
    private String employmentType;

    @Column(length = 30)
    private String education;

    @Column(length = 120)
    private String location;

    @Column(columnDefinition = "text")
    private String description;

    @Column(columnDefinition = "text")
    private String welfare;

    @Column(name = "salary_raw", columnDefinition = "text")
    private String salaryRaw;

    @Column(name = "salary_min")
    private Integer salaryMin;

    @Column(name = "salary_max")
    private Integer salaryMax;

    @Column(name = "salary_type", nullable = false, length = 16)
    @ColumnDefault("'unknown'")
    private String salaryType = "unknown";

    @Column(name = "body_is_image", nullable = false)
    @ColumnDefault("false")
    private boolean bodyImage;

    @Column(name = "posted_at")
    private OffsetDateTime postedAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "content_hash", length = 64, columnDefinition = "char(64)")
    private String contentHash;

    @Column(name = "embed_hash", length = 64, columnDefinition = "char(64)")
    private String embedHash;

    @Column(name = "collected_at", nullable = false)
    @ColumnDefault("now()")
    private OffsetDateTime collectedAt;

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

    @Column(name = "salary_period", length = 8)
    private String salaryPeriod;

    @Column(name = "body_extract_failed", nullable = false)
    @ColumnDefault("false")
    private boolean bodyExtractFailed;
}
