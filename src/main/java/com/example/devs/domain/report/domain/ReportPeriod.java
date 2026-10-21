package com.example.devs.domain.report.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportPeriod {
    WEEK(7),
    MONTH(30);

    private final int days;
}
