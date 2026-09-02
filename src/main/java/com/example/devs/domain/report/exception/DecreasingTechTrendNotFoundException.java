package com.example.devs.domain.report.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class DecreasingTechTrendNotFoundException extends DevsException {

    public DecreasingTechTrendNotFoundException() {
        super(ErrorCode.DECREASING_TECH_TREND_NOT_FOUND);
    }
}
