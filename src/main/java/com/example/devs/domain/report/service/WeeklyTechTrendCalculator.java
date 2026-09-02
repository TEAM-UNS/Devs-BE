package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.TechTrend;
import com.example.devs.domain.report.domain.repository.PostingSkillRepository;
import com.example.devs.domain.report.domain.repository.SkillCountProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WeeklyTechTrendCalculator {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");

    private final PostingSkillRepository postingSkillRepository;

    public List<TechTrend> calculate(LocalDate baseDate) {
        OffsetDateTime thisWeekStart = baseDate
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay(SEOUL)
                .toOffsetDateTime();
        OffsetDateTime lastWeekStart = thisWeekStart.minusWeeks(1);
        OffsetDateTime nextWeekStart = thisWeekStart.plusWeeks(1);

        List<SkillCountProjection> lastWeek =
                postingSkillRepository.findSkillCountsByPeriod(
                        lastWeekStart,
                        thisWeekStart
                );

        List<SkillCountProjection> thisWeek =
                postingSkillRepository.findSkillCountsByPeriod(
                        thisWeekStart,
                        nextWeekStart
                );

        return calculateChangeRates(lastWeek, thisWeek);
    }

    private List<TechTrend> calculateChangeRates(
            List<SkillCountProjection> lastWeek,
            List<SkillCountProjection> thisWeek
    ) {
        Map<Integer, Long> lastWeekMap = lastWeek.stream()
                .collect(Collectors.toMap(
                        SkillCountProjection::getSkillId,
                        SkillCountProjection::getMentionCount
                ));

        return thisWeek.stream()
                .map(skill -> {
                    long currentCount = skill.getMentionCount();

                    long previousCount = lastWeekMap.getOrDefault(
                            skill.getSkillId(),
                            0L
                    );

                    double changeRate =
                            calculateChangeRate(previousCount, currentCount);

                    return new TechTrend(
                            skill.getSkillId(),
                            skill.getSkillName(),
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
