package com.example.devs.domain.dashboard.presentation;

import com.example.devs.domain.dashboard.presentation.dto.response.DashboardSummaryResponse;
import com.example.devs.domain.dashboard.presentation.dto.response.PopularTechStackListResponse;
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
}
