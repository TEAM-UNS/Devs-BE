package com.example.devs.domain.retrospect_like.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RetrospectLikeNotFoundException extends DevsException {
    public RetrospectLikeNotFoundException() {
        super(ErrorCode.RETROSPECT_LIKE_NOT_FOUND);
    }
}
