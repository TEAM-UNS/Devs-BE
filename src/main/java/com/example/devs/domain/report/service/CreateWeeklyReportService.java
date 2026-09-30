package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.Report;
import com.example.devs.domain.report.domain.ReportPeriod;
import com.example.devs.domain.report.domain.TechTrend;
import com.example.devs.domain.report.domain.WeeklyReportStatistics;
import com.example.devs.domain.report.domain.repository.ReportRepository;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.exception.MajorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class CreateWeeklyReportService {
    private final ReportRepository reportRepository;
    private final TechFieldRepository techFieldRepository;
    private final PopularTechStackReportService popularTechStackReportService;
    private final TechMentionQueryService techMentionQueryService;
    private final WeeklyTechTrendCalculator weeklyTechTrendCalculator;
    private final WeeklyCollectedPostingCountService weeklyCollectedPostingCountService;
    private final EarliestPostingDateService earliestPostingDateService;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Report execute(Integer majorId, LocalDate baseDate, String llmReport) {
        var major = majorId == null ? null : techFieldRepository.findById(majorId)
                .orElseThrow(MajorNotFoundException::new);
        var popular = popularTechStackReportService.execute(majorId, ReportPeriod.WEEK, baseDate)
                .items().stream()
                .map(item -> new WeeklyReportStatistics.PopularTechStack(
                        item.rank(), item.techStackId(), item.name(), item.searchCount(),
                        item.previousSearchCount(), item.changeCount(), item.changeRate(), item.trend()
                )).toList();
        var mentions = techMentionQueryService.execute(majorId, baseDate).mentions().stream()
                .map(item -> new WeeklyReportStatistics.TechMention(
                        item.name(), item.previous(), item.current()
                )).toList();
        var trends = weeklyTechTrendCalculator.calculate(majorId, baseDate);
        var increase = trends.stream().filter(trend -> trend.changeRate() > 0)
                .max(Comparator.comparingDouble(TechTrend::changeRate)).orElse(null);
        var decrease = trends.stream().filter(trend -> trend.changeRate() < 0)
                .min(Comparator.comparingDouble(TechTrend::changeRate)).orElse(null);
        var statistics = new WeeklyReportStatistics(
                weeklyCollectedPostingCountService.execute(baseDate).count(),
                earliestPostingDateService.execute().earliestPostingDate(),
                popular, mentions, increase, decrease
        );
        return reportRepository.save(Report.createWeekly(major, baseDate, llmReport, statistics));
    }
}
