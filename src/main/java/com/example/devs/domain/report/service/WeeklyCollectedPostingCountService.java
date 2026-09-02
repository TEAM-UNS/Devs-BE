package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.repository.ReportQueryRepository;
import com.example.devs.domain.report.presentation.dto.response.WeeklyCollectedPostingCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
public class WeeklyCollectedPostingCountService {
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final ReportQueryRepository reportQueryRepository;
    private final Clock clock;

    @Transactional(readOnly = true)
    public WeeklyCollectedPostingCountResponse execute() {
        LocalDate today = clock.instant().atZone(SEOUL).toLocalDate();
        LocalDate monday = today.with(
                TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)
        );

        OffsetDateTime start = monday.atStartOfDay(SEOUL).toOffsetDateTime();
        OffsetDateTime end = monday.plusWeeks(1)
                .atStartOfDay(SEOUL)
                .toOffsetDateTime();

        long count = reportQueryRepository.countCollectedPostings(start, end);
        return new WeeklyCollectedPostingCountResponse(count);
    }
}
