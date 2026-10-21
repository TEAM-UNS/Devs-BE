package com.example.devs.domain.report.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class ReportNotFoundException extends DevsException {
    public ReportNotFoundException() {
        super(ErrorCode.REPORT_NOT_FOUND);
    }
}
