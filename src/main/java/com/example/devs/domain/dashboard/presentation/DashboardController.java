package com.example.devs.domain.dashboard.presentation;

import com.example.devs.domain.dashboard.presentation.dto.response.DashboardSummaryResponse;
import com.example.devs.domain.dashboard.service.DashboardSummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardSummaryService dashboardSummaryService;

    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return dashboardSummaryService.execute();
    }
}
