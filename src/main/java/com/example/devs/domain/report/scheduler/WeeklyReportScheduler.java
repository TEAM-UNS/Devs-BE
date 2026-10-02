package com.example.devs.domain.report.scheduler;

import com.example.devs.domain.report.domain.repository.ReportRepository;
import com.example.devs.domain.report.service.CreateWeeklyReportService;
import com.example.devs.domain.tech_field.domain.TechField;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/** 매주 월요일에 직전 주의 통계를 전체 및 전공별 리포트로 적재한다. LLM 요약은 이후에 채운다. */
@Slf4j
@Component
@RequiredArgsConstructor
public class WeeklyReportScheduler {
    private final CreateWeeklyReportService createWeeklyReportService;
    private final ReportRepository reportRepository;
    private final TechFieldRepository techFieldRepository;
    private final Clock clock;

    @Scheduled(cron = "0 10 0 * * MON", zone = "Asia/Seoul")
    public void createLastWeekReports() {
        LocalDate lastWeekStart = LocalDate.now(clock)
                .minusWeeks(1)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        List<Integer> majorIds = new ArrayList<>();
        majorIds.add(null);
        techFieldRepository.findAll().stream().map(TechField::getId).forEach(majorIds::add);

        for (Integer majorId : majorIds) {
            if (reportRepository.existsByMajor_IdAndWeekStartDate(majorId, lastWeekStart)) {
                continue;
            }
            try {
                createWeeklyReportService.execute(majorId, lastWeekStart, null);
            } catch (Exception e) {
                log.error("주간 리포트 생성 실패 majorId={}, weekStartDate={}", majorId, lastWeekStart, e);
            }
        }
    }
}
