package com.example.devs.domain.dashboard.scheduler;

import com.example.devs.domain.dashboard.service.DashboardSummaryCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class DashboardCacheScheduler {
    private final DashboardSummaryCacheService dashboardSummaryCacheService;

    @Scheduled(fixedDelay = 10, initialDelay = 0, timeUnit = TimeUnit.MINUTES)
    public void refreshSummary() {
        dashboardSummaryCacheService.refresh();
    }
}
