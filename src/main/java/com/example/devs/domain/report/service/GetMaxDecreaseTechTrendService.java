package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.TechTrend;
import com.example.devs.domain.report.exception.DecreasingTechTrendNotFoundException;
import com.example.devs.domain.report.presentation.dto.response.TechTrendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class GetMaxDecreaseTechTrendService {

    private final WeeklyTechTrendCalculator weeklyTechTrendCalculator;

    @Transactional(readOnly = true)
    public TechTrendResponse execute(LocalDate baseDate) {

        TechTrend trend = weeklyTechTrendCalculator.calculate(baseDate)
                .stream()
                .filter(it -> it.changeRate() < 0)
                .min(Comparator.comparingDouble(TechTrend::changeRate))
                .orElseThrow(DecreasingTechTrendNotFoundException::new);

        return new TechTrendResponse(
                trend.skillId(),
                trend.skillName(),
                Math.round(trend.changeRate() * 10) / 10.0
        );
    }
}
