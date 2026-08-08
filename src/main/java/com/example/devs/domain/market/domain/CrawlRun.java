package com.example.devs.domain.market.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;

import java.time.OffsetDateTime;

@Getter
@Entity
@Table(
        name = "crawl_run",
        schema = "market",
        indexes = {
                @Index(name = "crawl_run_kind_started_idx", columnList = "kind,started_at"),
                @Index(name = "crawl_run_source_started_idx", columnList = "source,started_at")
        }
)
@Check(name = "crawl_run_kind_chk", constraints = "kind IN ('crawl','embed')")
@Check(name = "crawl_run_status_chk", constraints = "status IN ('running','success','partial','failed')")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrawlRun {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 16)
    private String kind;

    @Column(length = 20)
    private String source;

    @Column(length = 120)
    private String keyword;

    @Column(nullable = false, length = 16)
    @ColumnDefault("'running'")
    private String status = "running";

    @Column(nullable = false)
    @ColumnDefault("0")
    private int fetched;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int inserted;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int updated;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int skipped;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int embedded;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int errors;

    @Column(columnDefinition = "text")
    private String message;

    @Column(name = "started_at", nullable = false)
    @ColumnDefault("now()")
    private OffsetDateTime startedAt;

    @Column(name = "finished_at")
    private OffsetDateTime finishedAt;
}
