package com.example.devs.domain.user.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class InvalidLoginCredentialsException extends DevsException {
    public InvalidLoginCredentialsException() {
        super(ErrorCode.INVALID_LOGIN_CREDENTIALS);
    }
}
