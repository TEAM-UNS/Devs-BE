package com.example.devs.domain.user.exception;

import com.example.devs.global.error.exception.ErrorCode;
import com.example.devs.global.error.exception.DevsException;

public class EmailAlreadyExistsException extends DevsException {
    public EmailAlreadyExistsException() {
        super(ErrorCode.EMAIL_ALREADY_EXISTS);
    }
}
