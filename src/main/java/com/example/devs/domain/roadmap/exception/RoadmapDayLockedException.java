package com.example.devs.domain.roadmap.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RoadmapDayLockedException extends DevsException {
    public RoadmapDayLockedException() {
        super(ErrorCode.ROADMAP_DAY_LOCKED);
    }
}
