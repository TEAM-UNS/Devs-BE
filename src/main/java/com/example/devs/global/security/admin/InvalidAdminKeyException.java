package com.example.devs.global.security.admin;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class InvalidAdminKeyException extends DevsException {
    public InvalidAdminKeyException() {
        super(ErrorCode.INVALID_ADMIN_KEY);
    }
}
