package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.TechTrend;
import com.example.devs.domain.report.domain.ReportWeek;
import com.example.devs.domain.report.domain.repository.PostingSkillRepository;
import com.example.devs.domain.report.domain.repository.SkillCountProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WeeklyTechTrendCalculator {

    private final PostingSkillRepository postingSkillRepository;

    public List<TechTrend> calculate(Integer majorId, LocalDate baseDate) {
        ReportWeek week = ReportWeek.from(baseDate);

        List<SkillCountProjection> lastWeek =
                postingSkillRepository.findSkillCountsByPeriod(
                        majorId,
                        week.previousStart(),
                        week.currentStart()
                );

        List<SkillCountProjection> thisWeek =
                postingSkillRepository.findSkillCountsByPeriod(
                        majorId,
                        week.currentStart(),
                        week.currentEnd()
                );

        return calculateChangeRates(lastWeek, thisWeek);
    }

    private List<TechTrend> calculateChangeRates(
            List<SkillCountProjection> lastWeek,
            List<SkillCountProjection> thisWeek
    ) {
        Map<Integer, Long> lastWeekMap = lastWeek.stream()
                .collect(Collectors.toMap(
                        SkillCountProjection::skillId,
                        SkillCountProjection::mentionCount
                ));

        return thisWeek.stream()
                .map(skill -> {
                    long currentCount = skill.mentionCount();

                    long previousCount = lastWeekMap.getOrDefault(
                            skill.skillId(),
                            0L
                    );

                    double changeRate =
                            calculateChangeRate(previousCount, currentCount);

                    return new TechTrend(
                            skill.skillId(),
                            skill.skillName(),
                            previousCount,
                            currentCount,
                            changeRate
                    );
                })
                .toList();
    }

    private double calculateChangeRate(
            long previousCount,
            long currentCount
    ) {
        if (previousCount == 0) {
            return currentCount == 0 ? 0.0 : 100.0;
        }

        return ((double) (currentCount - previousCount)
                / previousCount) * 100;
    }

}
