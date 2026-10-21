package com.example.devs.domain.user.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class EmailVerificationCodeMismatchException extends DevsException {
    public EmailVerificationCodeMismatchException() {
        super(ErrorCode.EMAIL_VERIFICATION_CODE_MISMATCH);
    }
}
