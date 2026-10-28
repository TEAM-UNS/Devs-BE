package com.example.devs.domain.roadmap.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RoadmapGenerationRequestFailedException extends DevsException {
    public RoadmapGenerationRequestFailedException() {
        super(ErrorCode.ROADMAP_GENERATION_REQUEST_FAILED);
    }
}
