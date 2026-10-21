package com.example.devs.domain.job_posting.domain;

import com.example.devs.domain.skill.domain.Skill;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@Table(
        name = "posting_skill",
        schema = "market",
        indexes = @Index(name = "posting_skill_skill_idx", columnList = "skill_id,requirement")
)
@Check(name = "posting_skill_requirement_chk", constraints = "requirement IN ('required','preferred','tag','body')")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostingSkill {
    @EmbeddedId
    private PostingSkillId id;

    @MapsId("postingId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "posting_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private JobPosting posting;

    @MapsId("skillId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "skill_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Skill skill;

    @Column(nullable = false, length = 16)
    private String requirement;

    @Column(nullable = false)
    @ColumnDefault("1")
    private int mentions = 1;
}
