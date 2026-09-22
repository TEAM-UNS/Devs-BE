package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.repository.ReportRepository;
import com.example.devs.domain.report.exception.ReportNotFoundException;
import com.example.devs.domain.report.presentation.dto.response.LlmReportQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetLlmQueryService {
    private final ReportRepository reportRepository;

    @Transactional(readOnly = true)
    public LlmReportQueryResponse getLlmReportQueryResponse(Long reportId) {
        return reportRepository.findById(reportId)
                .map(LlmReportQueryResponse::from)
                .orElseThrow(ReportNotFoundException::new);
    }
}
