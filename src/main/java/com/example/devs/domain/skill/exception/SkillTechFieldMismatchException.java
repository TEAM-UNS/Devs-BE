package com.example.devs.domain.skill.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class SkillTechFieldMismatchException extends DevsException {
    public SkillTechFieldMismatchException() {
        super(ErrorCode.SKILL_TECH_FIELD_MISMATCH);
    }
}
