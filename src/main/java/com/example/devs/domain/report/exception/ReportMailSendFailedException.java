package com.example.devs.domain.report.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class ReportMailSendFailedException extends DevsException {
    public ReportMailSendFailedException() {
        super(ErrorCode.REPORT_MAIL_SEND_FAILED);
    }
}
