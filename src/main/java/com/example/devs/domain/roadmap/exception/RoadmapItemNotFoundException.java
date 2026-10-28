package com.example.devs.domain.roadmap.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RoadmapItemNotFoundException extends DevsException {
    public RoadmapItemNotFoundException() {
        super(ErrorCode.ROADMAP_ITEM_NOT_FOUND);
    }
}
