package com.example.devs.domain.roadmap.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class RoadmapDayItemsNotDoneException extends DevsException {
    public RoadmapDayItemsNotDoneException() {
        super(ErrorCode.ROADMAP_DAY_ITEMS_NOT_DONE);
    }
}
