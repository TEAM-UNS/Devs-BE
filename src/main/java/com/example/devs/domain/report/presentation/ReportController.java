package com.example.devs.domain.report.presentation;

import com.example.devs.domain.report.domain.ReportPeriod;
import com.example.devs.domain.report.presentation.dto.response.LlmReportQueryResponse;
import com.example.devs.domain.report.service.GetLlmQueryService;
import com.example.devs.domain.report.presentation.dto.response.EarliestPostingDateResponse;
import com.example.devs.domain.report.service.EarliestPostingDateService;
import com.example.devs.domain.report.presentation.dto.response.PopularTechStackReportResponse;
import com.example.devs.domain.report.presentation.dto.response.TechMentionListResponse;
import com.example.devs.domain.report.presentation.dto.response.TechTrendResponse;
import com.example.devs.domain.report.presentation.dto.response.WeeklyCollectedPostingCountResponse;
import com.example.devs.domain.report.service.GetMaxDecreaseTechTrendService;
import com.example.devs.domain.report.service.GetMaxIncreaseTechTrendService;
import com.example.devs.domain.report.service.PopularTechStackReportService;
import com.example.devs.domain.report.service.TechMentionQueryService;
import com.example.devs.domain.report.service.WeeklyCollectedPostingCountService;
import com.example.devs.domain.report.presentation.dto.response.WeeklyReportsCreateResponse;
import com.example.devs.domain.report.service.CreateWeeklyReportsService;
import com.example.devs.global.security.admin.AdminKeyValidator;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    private final WeeklyCollectedPostingCountService weeklyCollectedPostingCountService;
    private final EarliestPostingDateService earliestPostingDateService;
    private final GetLlmQueryService getLlmQueryService;
    private final CreateWeeklyReportsService createWeeklyReportsService;
    private final AdminKeyValidator adminKeyValidator;

    @PostMapping("/weekly")
    @ResponseStatus(HttpStatus.CREATED)
    public WeeklyReportsCreateResponse createWeeklyReports(
            @RequestHeader(name = "X-Admin-Key", required = false) String adminKey,
            @RequestParam(name = "base_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate baseDate
    ) {
        adminKeyValidator.validate(adminKey);
        return createWeeklyReportsService.execute(baseDate);
    }

    @GetMapping("/llm/{reportId}")
    public LlmReportQueryResponse getLlmReport(
            @PathVariable("reportId") @Positive Long reportId
    ) {
        return getLlmQueryService.getLlmReportQueryResponse(reportId);
    }

    @GetMapping("/earliest-posting-date")
    public EarliestPostingDateResponse getEarliestPostingDate() {
        return earliestPostingDateService.execute();
    }

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

    @GetMapping("/weekly-collected-count")
    public WeeklyCollectedPostingCountResponse getWeeklyCollectedPostingCount(
            @RequestParam(name = "base_date")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate baseDate
    ) {
        return weeklyCollectedPostingCountService.execute(baseDate);
    }
}
