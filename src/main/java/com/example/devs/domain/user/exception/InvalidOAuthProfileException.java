package com.example.devs.domain.user.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class InvalidOAuthProfileException extends DevsException {
    public InvalidOAuthProfileException() {
        super(ErrorCode.INVALID_OAUTH_PROFILE);
    }
}
