package com.example.devs.domain.report.presentation.dto.response;

import com.example.devs.domain.report.domain.ReportPeriod;

import java.time.LocalDate;
import java.util.List;

public record PopularTechStackReportResponse(
        Integer majorId,
        ReportPeriod period,
        LocalDate baseDate,
        List<PopularTechStackReportItemResponse> items
) {
}
