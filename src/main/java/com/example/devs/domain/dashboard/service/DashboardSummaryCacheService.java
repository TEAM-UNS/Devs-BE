package com.example.devs.domain.dashboard.service;

import com.example.devs.domain.dashboard.presentation.dto.response.DashboardSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardSummaryCacheService {
    private static final String KEY = "dashboard:summary:v1";
    private static final Duration TTL = Duration.ofMinutes(30);

    private final DashboardSummaryService dashboardSummaryService;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public DashboardSummaryResponse getDashboardSummary() {
        DashboardSummaryResponse cached = read();
        if (cached != null) {
            return cached;
        }
        return refresh();
    }

    public DashboardSummaryResponse refresh() {
        DashboardSummaryResponse summary = dashboardSummaryService.execute();
        write(summary);
        return summary;
    }

    private DashboardSummaryResponse read() {
        try {
            String json = stringRedisTemplate.opsForValue().get(KEY);
            return json == null ? null : objectMapper.readValue(json, DashboardSummaryResponse.class);
        } catch (Exception e) {
            log.warn("대시보드 요약 캐시 읽기 실패", e);
            return null;
        }
    }

    private void write(DashboardSummaryResponse summary) {
        try {
            stringRedisTemplate.opsForValue().set(KEY, objectMapper.writeValueAsString(summary), TTL);
        } catch (Exception e) {
            log.warn("대시보드 요약 캐시 저장 실패", e);
        }
    }
}
