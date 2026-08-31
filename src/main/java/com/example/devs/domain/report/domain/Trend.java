package com.example.devs.domain.report.domain;

public enum Trend {
    UP,
    DOWN,
    SAME;

    public static Trend from(long changeCount){
        if(changeCount > 0) {
            return UP;
        }
        if(changeCount < 0) {
            return DOWN;
        }
        return SAME;
    }
}
