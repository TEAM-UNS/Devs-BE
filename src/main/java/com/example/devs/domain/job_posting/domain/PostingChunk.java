package com.example.devs.domain.job_posting.domain;

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
        name = "posting_chunk",
        schema = "market",
        uniqueConstraints = @UniqueConstraint(
                name = "posting_chunk_uk",
                columnNames = {"posting_id", "section", "seq"}
        ),
        indexes = @Index(name = "posting_chunk_section_idx", columnList = "section")
)
@Check(name = "posting_chunk_section_chk", constraints = "section IN ('responsibility','required','preferred')")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostingChunk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "posting_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private JobPosting posting;

    @Column(nullable = false, length = 20)
    private String section;

    @Column(nullable = false)
    @ColumnDefault("0")
    private int seq;

    @Column(nullable = false, columnDefinition = "text")
    private String content;

    @Column(name = "chunk_hash", nullable = false, length = 64, columnDefinition = "char(64)")
    private String chunkHash;

    @Column(columnDefinition = "vector(1024)")
    @ColumnTransformer(write = "cast(? as vector)")
    private String embedding;

    @Column(name = "token_count")
    private Integer tokenCount;

    @Column(name = "created_at", nullable = false)
    @ColumnDefault("now()")
    private OffsetDateTime createdAt;
}
