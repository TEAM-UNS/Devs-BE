package com.example.devs.domain.report.presentation.dto.response;

import com.example.devs.domain.report.domain.Report;
import lombok.Builder;

@Builder
public record LlmReportQueryResponse(
        String llmReport
) {
    public static LlmReportQueryResponse from(Report report) {
        return LlmReportQueryResponse.builder()
                .llmReport(report.getLlmReport())
                .build();
    }
}
