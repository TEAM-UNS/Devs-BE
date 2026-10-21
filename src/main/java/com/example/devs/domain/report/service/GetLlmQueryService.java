package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.repository.ReportRepository;
import com.example.devs.domain.report.exception.ReportNotFoundException;
import com.example.devs.domain.report.presentation.dto.response.LlmReportQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Service
@RequiredArgsConstructor
public class GetLlmQueryService {
    private final ReportRepository reportRepository;

    /** majorId가 null이면 전체 탭 리포트를 찾는다. 요약이 아직 생성 중이면 llmReport가 null이다. */
    @Transactional(readOnly = true)
    public LlmReportQueryResponse execute(Integer majorId, LocalDate baseDate) {
        LocalDate weekStartDate = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        return reportRepository.findFirstByMajor_IdAndWeekStartDateOrderByCreatedAtDescIdDesc(majorId, weekStartDate)
                .map(LlmReportQueryResponse::from)
                .orElseThrow(ReportNotFoundException::new);
    }
}
