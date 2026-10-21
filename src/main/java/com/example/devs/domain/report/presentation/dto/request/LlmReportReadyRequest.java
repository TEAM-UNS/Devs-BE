package com.example.devs.domain.report.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

// AI 서버가 snake_case로 보낸다.
public record LlmReportReadyRequest(
        @NotNull @JsonProperty("week_start_date") LocalDate weekStartDate,
        int total,
        int generated,
        int skipped,
        // 아직 생성하지 못한 수. 0이면 그 주차 리포트가 모두 채워진 것이다.
        @NotNull Integer pending
) {}
