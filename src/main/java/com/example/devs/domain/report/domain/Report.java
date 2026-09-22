package com.example.devs.domain.report.domain;

import com.example.devs.domain.tech_field.domain.TechField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Objects;

@Getter
@Entity
@Table(
        name = "report",
        schema = "market",
        indexes = @Index(
                name = "report_major_week_created_idx",
                columnList = "major_id,week_start_date,created_at DESC,id DESC"
        )
)
@Check(name = "report_week_start_chk", constraints = "extract(isodow from week_start_date) = 1")
@Check(name = "report_llm_report_chk", constraints = "length(trim(llm_report)) > 0")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "major_id", foreignKey = @ForeignKey(name = "report_major_fk"))
    private TechField major;

    @Column(name = "week_start_date", nullable = false)
    private LocalDate weekStartDate;

    @Column(name = "llm_report", nullable = false, columnDefinition = "text")
    private String llmReport;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    public static Report createWeekly(TechField major, LocalDate baseDate, String llmReport) {
        Objects.requireNonNull(baseDate, "baseDate must not be null");
        if (llmReport == null || llmReport.isBlank()) {
            throw new IllegalArgumentException("llmReport must not be blank");
        }

        Report report = new Report();
        report.major = major;
        report.weekStartDate = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        report.llmReport = llmReport;
        return report;
    }
}
