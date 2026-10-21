package com.example.devs.domain.user.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class EmailSendFailedException extends DevsException {
    public EmailSendFailedException() {
        super(ErrorCode.EMAIL_SEND_FAILED);
    }
}
