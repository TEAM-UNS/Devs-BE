package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.ReportWeek;
import com.example.devs.domain.report.presentation.dto.response.ReportWeekRangeResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReportWeekRangeService {

    public ReportWeekRangeResponse execute(LocalDate baseDate){
        return ReportWeekRangeResponse.from(ReportWeek.from(baseDate));
    }
}
