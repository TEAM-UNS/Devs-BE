package com.example.devs.global.error.exception;

import lombok.Getter;

@Getter
public class DevsException extends RuntimeException {
    private final ErrorCode errorCode;

    public DevsException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
