package com.example.devs.domain.user.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class EmailVerificationRequestLimitException extends DevsException {
    public EmailVerificationRequestLimitException() {
        super(ErrorCode.EMAIL_VERIFICATION_REQUEST_LIMIT);
    }
}
