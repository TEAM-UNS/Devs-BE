package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.repository.ReportRepository;
import com.example.devs.domain.report.exception.ReportWeekNotFinishedException;
import com.example.devs.domain.report.presentation.dto.response.WeeklyReportsCreateResponse;
import com.example.devs.domain.tech_field.domain.TechField;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

/** 한 주의 리포트를 전체(major 없음) 및 전공별로 적재한다. 이미 있는 리포트는 건너뛴다. */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateWeeklyReportsService {
    private final CreateWeeklyReportService createWeeklyReportService;
    private final ReportRepository reportRepository;
    private final TechFieldRepository techFieldRepository;
    private final Clock clock;

    public WeeklyReportsCreateResponse execute(LocalDate baseDate) {
        LocalDate weekStart = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        if (weekStart.plusWeeks(1).isAfter(LocalDate.now(clock))) {
            throw new ReportWeekNotFinishedException();
        }

        List<Integer> majorIds = new ArrayList<>();
        majorIds.add(null);
        techFieldRepository.findAll().stream().map(TechField::getId).forEach(majorIds::add);

        int created = 0;
        int skipped = 0;
        int failed = 0;
        for (Integer majorId : majorIds) {
            if (reportRepository.existsByMajor_IdAndWeekStartDate(majorId, weekStart)) {
                skipped++;
                continue;
            }
            try {
                createWeeklyReportService.execute(majorId, weekStart, null);
                created++;
            } catch (Exception e) {
                failed++;
                log.error("주간 리포트 생성 실패 majorId={}, weekStartDate={}", majorId, weekStart, e);
            }
        }
        return new WeeklyReportsCreateResponse(weekStart, created, skipped, failed);
    }
}
