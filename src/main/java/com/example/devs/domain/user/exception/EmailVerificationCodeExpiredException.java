package com.example.devs.domain.user.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class EmailVerificationCodeExpiredException extends DevsException {
    public EmailVerificationCodeExpiredException() {
        super(ErrorCode.EMAIL_VERIFICATION_CODE_EXPIRED);
    }
}
