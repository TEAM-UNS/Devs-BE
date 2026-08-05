package com.example.devs.domain.tech_stack.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class TechStackNotFoundException extends DevsException {
    public TechStackNotFoundException() {
        super(ErrorCode.TECH_STACK_NOT_FOUND);
    }
}
