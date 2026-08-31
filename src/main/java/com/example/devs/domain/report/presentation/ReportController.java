package com.example.devs.domain.report.presentation;

import com.example.devs.domain.report.domain.ReportPeriod;
import com.example.devs.domain.report.presentation.dto.response.PopularTechStackReportResponse;
import com.example.devs.domain.report.service.PopularTechStackReportService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final PopularTechStackReportService popularTechStackReportService;

    @GetMapping("/popular-tech-stack")
    public PopularTechStackReportResponse getPopularTechStackReport(
            @RequestParam(name = "major_id") @Positive Integer majorId,
            @RequestParam ReportPeriod period,
            @RequestParam(name = "base_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate baseDate
    ) {
        return popularTechStackReportService.execute(majorId, period, baseDate);
    }
}
