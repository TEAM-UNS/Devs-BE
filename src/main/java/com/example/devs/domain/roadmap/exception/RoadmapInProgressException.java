package com.example.devs.domain.roadmap.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RoadmapInProgressException extends DevsException {
    public RoadmapInProgressException() {
        super(ErrorCode.ROADMAP_IN_PROGRESS);
    }
}
