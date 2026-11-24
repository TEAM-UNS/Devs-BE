package com.example.devs.domain.roadmap.scheduler;

import com.example.devs.domain.roadmap.service.RoadmapGenerationTimeoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RoadmapGenerationTimeoutScheduler {
    private final RoadmapGenerationTimeoutService roadmapGenerationTimeoutService;

    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.MINUTES)
    public void failTimedOutRoadmaps() {
        roadmapGenerationTimeoutService.execute();
    }
}
