package com.example.devs.domain.roadmap.domain;

public enum RoadmapStatus {
    PENDING("pending"),
    GENERATING("generating"),
    COMPLETED("completed"),
    FAILED("failed");

    private final String databaseValue;

    RoadmapStatus(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public String databaseValue() {
        return databaseValue;
    }

    public static RoadmapStatus from(String databaseValue) {
        for (RoadmapStatus status : values()) {
            if (status.databaseValue.equals(databaseValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("알 수 없는 로드맵 상태입니다: " + databaseValue);
    }
}
