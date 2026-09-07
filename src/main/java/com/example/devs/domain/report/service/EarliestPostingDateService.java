package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.repository.ReportQueryRepository;
import com.example.devs.domain.report.presentation.dto.response.EarliestPostingDateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class EarliestPostingDateService {
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private final ReportQueryRepository reportQueryRepository;

    @Transactional(readOnly = true)
    public EarliestPostingDateResponse execute() {
        var earliest = reportQueryRepository.findEarliestPostedAt();
        return new EarliestPostingDateResponse(
                earliest == null ? null : earliest.atZoneSameInstant(SEOUL).toLocalDate()
        );
    }
}
