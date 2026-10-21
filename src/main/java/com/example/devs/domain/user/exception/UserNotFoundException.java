package com.example.devs.domain.user.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class UserNotFoundException extends DevsException {
    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
