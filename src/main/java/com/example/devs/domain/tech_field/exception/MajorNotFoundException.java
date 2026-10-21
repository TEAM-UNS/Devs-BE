package com.example.devs.domain.tech_field.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class MajorNotFoundException extends DevsException {
    public MajorNotFoundException() {
        super(ErrorCode.MAJOR_NOT_FOUND);
    }
}
