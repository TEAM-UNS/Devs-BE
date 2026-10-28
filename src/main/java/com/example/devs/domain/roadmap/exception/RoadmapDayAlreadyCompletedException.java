package com.example.devs.domain.roadmap.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RoadmapDayAlreadyCompletedException extends DevsException {
    public RoadmapDayAlreadyCompletedException() {
        super(ErrorCode.ROADMAP_DAY_ALREADY_COMPLETED);
    }
}
