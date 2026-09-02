package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.ReportPeriod;
import com.example.devs.domain.report.domain.Trend;
import com.example.devs.domain.report.domain.repository.ReportQueryRepository;
import com.example.devs.domain.report.presentation.dto.response.PopularTechStackReportItemResponse;
import com.example.devs.domain.report.presentation.dto.response.PopularTechStackReportResponse;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.exception.MajorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class PopularTechStackReportService {

    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final int LIMIT = 10;

    private final ReportQueryRepository reportQueryRepository;
    private final TechFieldRepository techFieldRepository;

    @Transactional(readOnly = true)
    public PopularTechStackReportResponse execute(
            Integer majorId,
            ReportPeriod period,
            LocalDate baseDate
    ) {
        if (!techFieldRepository.existsById(majorId)) {
            throw new MajorNotFoundException();
        }

        ReportDateRange range = calculateDateRange(period, baseDate);

        List<ReportQueryRepository.TechStackCount> counts =
                reportQueryRepository.findPopularTechStacks(
                        majorId,
                        range.previousStart(),
                        range.currentStart(),
                        range.currentEnd(),
                        LIMIT
                );
        List<PopularTechStackReportItemResponse> items = IntStream.range(0, counts.size())
                .mapToObj(index -> toResponse(index + 1, counts.get(index))).toList();

        return new PopularTechStackReportResponse(
                majorId,
                period,
                baseDate,
                items
        );
    }
    private ReportDateRange calculateDateRange(ReportPeriod period, LocalDate baseDate) {
        int periodDays = period.getDays();

        LocalDate currentStartDate = baseDate.minusDays(periodDays - 1L);

        LocalDate currentEndDate = baseDate.plusDays(1);

        LocalDate previousStartDate =
                currentStartDate.minusDays(periodDays);

        return new ReportDateRange(
                previousStartDate.atStartOfDay(SEOUL).toOffsetDateTime(),
                currentStartDate.atStartOfDay(SEOUL).toOffsetDateTime(),
                currentEndDate.atStartOfDay(SEOUL).toOffsetDateTime()
        );
    }

    private PopularTechStackReportItemResponse toResponse(
            int rank,
            ReportQueryRepository.TechStackCount count
    ) {
        long changeCount =
                count.currentCount() - count.previousCount();

        Double changeRate = calculateChangeRate(
                changeCount,
                count.previousCount()
        );

        return new PopularTechStackReportItemResponse(
                rank,
                count.techStackId(),
                count.name(),
                count.currentCount(),
                count.previousCount(),
                changeCount,
                changeRate,
                Trend.from(changeCount)
        );
    }

    private Double calculateChangeRate(
            long changeCount,
            long previousCount
    ) {
        if (previousCount == 0) {
            return null;
        }

        return BigDecimal.valueOf(changeCount)
                .multiply(BigDecimal.valueOf(100))
                .divide(
                        BigDecimal.valueOf(previousCount),
                        2,
                        RoundingMode.HALF_UP
                )
                .doubleValue();
    }

    private record ReportDateRange(
            OffsetDateTime previousStart,
            OffsetDateTime currentStart,
            OffsetDateTime currentEnd
    ) {
    }
}
