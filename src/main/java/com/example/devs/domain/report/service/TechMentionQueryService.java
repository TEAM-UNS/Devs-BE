package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.repository.ReportQueryRepository;
import com.example.devs.domain.report.presentation.dto.response.TechMentionListResponse;
import com.example.devs.domain.report.presentation.dto.response.TechMentionResponse;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.exception.MajorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TechMentionQueryService {
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final int PERIOD_DAYS = 7;
    private static final int TECH_MENTION_LIMIT = 5;

    private final ReportQueryRepository reportQueryRepository;
    private final TechFieldRepository techFieldRepository;

    @Transactional(readOnly = true)
    public TechMentionListResponse execute(Integer majorId, LocalDate baseDate) {
        if (majorId != null && !techFieldRepository.existsById(majorId)) {
            throw new MajorNotFoundException();
        }

        OffsetDateTime currentStart = baseDate.minusDays(PERIOD_DAYS - 1L)
                .atStartOfDay(SEOUL)
                .toOffsetDateTime();
        OffsetDateTime currentEnd = baseDate.plusDays(1)
                .atStartOfDay(SEOUL)
                .toOffsetDateTime();
        OffsetDateTime previousStart = currentStart.minusDays(PERIOD_DAYS);

        List<ReportQueryRepository.TechStackCount> counts = majorId == null
                ? reportQueryRepository.findTechMentions(
                        previousStart,
                        currentStart,
                        currentEnd,
                        TECH_MENTION_LIMIT
                )
                : reportQueryRepository.findPopularTechStacks(
                        majorId,
                        previousStart,
                        currentStart,
                        currentEnd,
                        TECH_MENTION_LIMIT
                );

        List<TechMentionResponse> mentions = counts.stream()
                .map(count -> new TechMentionResponse(
                        count.name(),
                        count.previousCount(),
                        count.currentCount()
                ))
                .toList();

        return new TechMentionListResponse(mentions);
    }
}
