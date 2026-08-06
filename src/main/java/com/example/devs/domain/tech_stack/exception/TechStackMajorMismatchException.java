package com.example.devs.domain.tech_stack.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class TechStackMajorMismatchException extends DevsException {
    public TechStackMajorMismatchException() {
        super(ErrorCode.TECH_STACK_MAJOR_MISMATCH);
    }
}
