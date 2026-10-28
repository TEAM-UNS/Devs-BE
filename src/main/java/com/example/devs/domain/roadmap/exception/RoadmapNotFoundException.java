package com.example.devs.domain.roadmap.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RoadmapNotFoundException extends DevsException {
    public RoadmapNotFoundException() {
        super(ErrorCode.ROADMAP_NOT_FOUND);
    }
}
