package com.example.devs.domain.roadmap.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RoadmapDailyLimitExceededException extends DevsException {
    public RoadmapDailyLimitExceededException() {
        super(ErrorCode.ROADMAP_DAILY_LIMIT_EXCEEDED);
    }
}
