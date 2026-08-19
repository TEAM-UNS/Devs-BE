package com.example.devs.domain.dashboard.service;

import com.example.devs.domain.dashboard.domain.TechTrendPeriod;
import com.example.devs.domain.dashboard.domain.repository.DashboardQueryRepository;
import com.example.devs.domain.dashboard.presentation.dto.response.BestTechStackListResponse;
import com.example.devs.domain.dashboard.presentation.dto.response.BestTechStackResponse;
import com.example.devs.domain.dashboard.presentation.dto.response.TechStackTrendValueResponse;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.exception.MajorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BestTechStackQueryService {
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final int BEST_TECH_STACK_LIMIT = 2;

    private final DashboardQueryRepository dashboardQueryRepository;
    private final TechFieldRepository techFieldRepository;
    private final Clock clock;

    @Transactional(readOnly = true)
    public BestTechStackListResponse execute(TechTrendPeriod period, Integer majorId) {
        if (majorId != null && !techFieldRepository.existsById(majorId)) {
            throw new MajorNotFoundException();
        }

        LocalDate today = clock.instant().atZone(SEOUL).toLocalDate();
        List<LocalDate> bucketDates = period.bucketDates(today);
        OffsetDateTime start = period.firstBucket(today)
                .atStartOfDay(SEOUL)
                .toOffsetDateTime();
        OffsetDateTime end = period.endExclusive(today)
                .atStartOfDay(SEOUL)
                .toOffsetDateTime();

        List<DashboardQueryRepository.TechStackTrendPoint> points = majorId == null
                ? dashboardQueryRepository.findTechStackTrendPoints(
                        start,
                        end,
                        period.bucketUnit()
                )
                : dashboardQueryRepository.findTechStackTrendPointsByMajorId(
                        majorId,
                        start,
                        end,
                        period.bucketUnit()
                );

        Map<Integer, TrendSeries> seriesByTechStackId = new LinkedHashMap<>();
        for (DashboardQueryRepository.TechStackTrendPoint point : points) {
            TrendSeries series = seriesByTechStackId.computeIfAbsent(
                    point.id(),
                    id -> new TrendSeries(id, point.name(), new HashMap<>())
            );
            series.values().put(point.date(), point.value());
        }

        List<BestTechStackResponse> techStacks = seriesByTechStackId.values().stream()
                .map(series -> toCandidate(series, bucketDates))
                .filter(candidate -> candidate.growth() > 0)
                .sorted(Comparator.comparingLong(TrendCandidate::growth)
                        .reversed()
                        .thenComparing(
                                Comparator.comparingLong(TrendCandidate::latestValue).reversed()
                        )
                        .thenComparing(candidate -> candidate.series().name()))
                .limit(BEST_TECH_STACK_LIMIT)
                .map(candidate -> toResponse(candidate.series(), bucketDates))
                .toList();

        return new BestTechStackListResponse(period, majorId, techStacks);
    }

    private TrendCandidate toCandidate(TrendSeries series, List<LocalDate> bucketDates) {
        long firstValue = series.values().getOrDefault(bucketDates.getFirst(), 0L);
        long latestValue = series.values().getOrDefault(bucketDates.getLast(), 0L);

        return new TrendCandidate(series, latestValue - firstValue, latestValue);
    }

    private BestTechStackResponse toResponse(
            TrendSeries series,
            List<LocalDate> bucketDates
    ) {
        List<TechStackTrendValueResponse> values = bucketDates.stream()
                .map(date -> new TechStackTrendValueResponse(
                        date,
                        series.values().getOrDefault(date, 0L)
                ))
                .toList();

        return new BestTechStackResponse(series.id(), series.name(), values);
    }

    private record TrendSeries(
            Integer id,
            String name,
            Map<LocalDate, Long> values
    ) {
    }

    private record TrendCandidate(
            TrendSeries series,
            long growth,
            long latestValue
    ) {
    }
}
