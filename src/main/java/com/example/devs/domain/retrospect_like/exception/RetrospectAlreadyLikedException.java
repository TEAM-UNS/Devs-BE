package com.example.devs.domain.retrospect_like.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RetrospectAlreadyLikedException extends DevsException {
    public RetrospectAlreadyLikedException() {
        super(ErrorCode.RETROSPECT_ALREADY_LIKED);
    }
}
