package com.example.devs.domain.company.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class InvalidCompareCompanyCountException extends DevsException {
    public InvalidCompareCompanyCountException() {
        super(ErrorCode.INVALID_COMPARE_COMPANY_COUNT);
    }
}
