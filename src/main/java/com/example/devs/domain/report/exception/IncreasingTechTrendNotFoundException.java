package com.example.devs.domain.report.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class IncreasingTechTrendNotFoundException extends DevsException {

    public IncreasingTechTrendNotFoundException() {
        super(ErrorCode.INCREASING_TECH_TREND_NOT_FOUND);
    }
}
