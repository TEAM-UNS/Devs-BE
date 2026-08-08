package com.example.devs.domain.market.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SkillFieldId implements Serializable {
    @Column(name = "skill_id", nullable = false)
    private Integer skillId;

    @Column(name = "field_id", nullable = false)
    private Integer fieldId;
}
