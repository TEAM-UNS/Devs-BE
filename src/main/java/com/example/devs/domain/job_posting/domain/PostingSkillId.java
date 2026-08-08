package com.example.devs.domain.job_posting.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostingSkillId implements Serializable {
    @Column(name = "posting_id", nullable = false)
    private Long postingId;

    @Column(name = "skill_id", nullable = false)
    private Integer skillId;
}
