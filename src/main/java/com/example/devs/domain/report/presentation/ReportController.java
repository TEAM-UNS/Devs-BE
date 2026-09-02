package com.example.devs.domain.report.presentation;

import com.example.devs.domain.report.domain.ReportPeriod;
import com.example.devs.domain.report.presentation.dto.response.PopularTechStackReportResponse;
import com.example.devs.domain.report.presentation.dto.response.TechMentionListResponse;
import com.example.devs.domain.report.presentation.dto.response.TechTrendResponse;
import com.example.devs.domain.report.service.GetMaxDecreaseTechTrendService;
import com.example.devs.domain.report.service.GetMaxIncreaseTechTrendService;
import com.example.devs.domain.report.service.PopularTechStackReportService;
import com.example.devs.domain.report.service.TechMentionQueryService;
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
    private final GetMaxIncreaseTechTrendService getMaxIncreaseTechTrendService;
    private final GetMaxDecreaseTechTrendService getMaxDecreaseTechTrendService;
    private final TechMentionQueryService techMentionQueryService;

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

    @GetMapping("/max-increase")
    public TechTrendResponse getMaxIncrease(
            @RequestParam(name = "major_id", required = false) @Positive Integer majorId,
            @RequestParam(name = "base_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate baseDate
    ) {
        return getMaxIncreaseTechTrendService.execute(majorId, baseDate);
    }

    @GetMapping("/max-decrease")
    public TechTrendResponse getMaxDecrease(
            @RequestParam(name = "major_id", required = false) @Positive Integer majorId,
            @RequestParam(name = "base_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate baseDate
    ) {
        return getMaxDecreaseTechTrendService.execute(majorId, baseDate);
    }

    @GetMapping("/tech-mentions")
    public TechMentionListResponse getTechMentions(
            @RequestParam(name = "major_id", required = false) @Positive Integer majorId,
            @RequestParam(name = "base_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate baseDate
    ) {
        return techMentionQueryService.execute(majorId, baseDate);
    }
}
