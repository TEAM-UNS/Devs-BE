package com.example.devs.domain.dashboard.presentation;

import com.example.devs.domain.company.domain.CompanySize;
import com.example.devs.domain.dashboard.domain.TechTrendPeriod;
import com.example.devs.domain.dashboard.presentation.dto.response.BestTechStackListResponse;
import com.example.devs.domain.dashboard.presentation.dto.response.CompanySizeTechStackListResponse;
import com.example.devs.domain.dashboard.presentation.dto.response.DashboardSummaryResponse;
import com.example.devs.domain.dashboard.presentation.dto.response.PopularTechStackListResponse;
import com.example.devs.domain.dashboard.service.BestTechStackQueryService;
import com.example.devs.domain.dashboard.service.CompanySizeTechStackQueryService;
import com.example.devs.domain.dashboard.service.DashboardSummaryService;
import com.example.devs.domain.dashboard.service.PopularTechStackQueryService;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardSummaryService dashboardSummaryService;
    private final PopularTechStackQueryService popularTechStackQueryService;
    private final CompanySizeTechStackQueryService companySizeTechStackQueryService;
    private final BestTechStackQueryService bestTechStackQueryService;

    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return dashboardSummaryService.execute();
    }

    @GetMapping("/popular-tech-stacks")
    public PopularTechStackListResponse getPopularTechStacks(
            @RequestParam(required = false)
            @Positive
            Integer majorId
    ) {
        return popularTechStackQueryService.execute(majorId);
    }

    @GetMapping("/company-size-tech-stacks")
    public CompanySizeTechStackListResponse getCompanySizeTechStacks(
            @RequestParam CompanySize companySize,
            @RequestParam @Positive Integer majorId
    ) {
        return companySizeTechStackQueryService.execute(companySize, majorId);
    }

    @GetMapping("/best-tech-stacks")
    public BestTechStackListResponse getBestTechStacks(
            @RequestParam TechTrendPeriod period,
            @RequestParam @Positive Integer majorId
    ) {
        return bestTechStackQueryService.execute(period, majorId);
    }
}
