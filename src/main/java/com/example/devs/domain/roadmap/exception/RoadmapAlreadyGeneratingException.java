package com.example.devs.domain.roadmap.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RoadmapAlreadyGeneratingException extends DevsException {
    public RoadmapAlreadyGeneratingException() {
        super(ErrorCode.ROADMAP_ALREADY_GENERATING);
    }
}
