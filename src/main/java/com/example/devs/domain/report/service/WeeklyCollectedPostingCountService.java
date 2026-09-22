package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.ReportWeek;
import com.example.devs.domain.report.domain.repository.ReportQueryRepository;
import com.example.devs.domain.report.presentation.dto.response.WeeklyCollectedPostingCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class WeeklyCollectedPostingCountService {
    private final ReportQueryRepository reportQueryRepository;

    @Transactional(readOnly = true)
    public WeeklyCollectedPostingCountResponse execute(LocalDate baseDate) {
        ReportWeek week = ReportWeek.from(baseDate);
        long count = reportQueryRepository.countCollectedPostings(
                week.currentStart(), week.currentEnd()
        );
        return new WeeklyCollectedPostingCountResponse(count);
    }
}
