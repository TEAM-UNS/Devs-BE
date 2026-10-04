package com.example.devs.domain.user.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class UnauthenticatedUserException extends DevsException {
    public UnauthenticatedUserException() {
        super(ErrorCode.UNAUTHENTICATED_USER);
    }
}
