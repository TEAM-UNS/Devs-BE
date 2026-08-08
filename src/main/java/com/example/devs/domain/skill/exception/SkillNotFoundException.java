package com.example.devs.domain.skill.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class SkillNotFoundException extends DevsException {
    public SkillNotFoundException() {
        super(ErrorCode.SKILL_NOT_FOUND);
    }
}
