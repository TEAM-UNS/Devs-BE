package com.example.devs.domain.retrospect.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RetrospectNotFoundException extends DevsException {
    public RetrospectNotFoundException() {
        super(ErrorCode.RETROSPECT_NOT_FOUND);
    }
}
