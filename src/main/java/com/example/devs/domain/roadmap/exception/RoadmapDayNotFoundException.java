package com.example.devs.domain.roadmap.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RoadmapDayNotFoundException extends DevsException {
    public RoadmapDayNotFoundException() {
        super(ErrorCode.ROADMAP_DAY_NOT_FOUND);
    }
}
