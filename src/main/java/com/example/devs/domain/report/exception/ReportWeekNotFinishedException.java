package com.example.devs.domain.report.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class ReportWeekNotFinishedException extends DevsException {
    public ReportWeekNotFinishedException() {
        super(ErrorCode.REPORT_WEEK_NOT_FINISHED);
    }
}
