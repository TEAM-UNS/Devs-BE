package com.example.devs.domain.dashboard.service;

import com.example.devs.domain.dashboard.domain.repository.DashboardQueryRepository;
import com.example.devs.domain.dashboard.presentation.dto.response.DashboardSummaryResponse;
import com.example.devs.domain.dashboard.presentation.dto.response.MentionedTechResponse;
import com.example.devs.domain.dashboard.presentation.dto.response.RisingTechResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class DashboardSummaryService {
    private static final ZoneId SEOUL = ZoneId.of("Asia/Seoul");
    private static final int MENTION_PERIOD_DAYS = 30;
    private static final int RISING_PERIOD_DAYS = 7;
    private static final int MINIMUM_PREVIOUS_MENTIONS = 3;

    private final DashboardQueryRepository dashboardQueryRepository;
    private final Clock clock;

    @Transactional(readOnly = true)
    public DashboardSummaryResponse execute() {
        OffsetDateTime now = clock.instant()
                .atZone(SEOUL)
                .toOffsetDateTime();
        LocalDate today = now.toLocalDate();

        OffsetDateTime todayStart = today.atStartOfDay(SEOUL).toOffsetDateTime();
        OffsetDateTime tomorrowStart = today.plusDays(1).atStartOfDay(SEOUL).toOffsetDateTime();
        OffsetDateTime yesterdayStart = today.minusDays(1).atStartOfDay(SEOUL).toOffsetDateTime();

        long todayCollectedCount = dashboardQueryRepository.countCollectedBetween(
                todayStart,
                tomorrowStart
        );
        long yesterdayCollectedCount = dashboardQueryRepository.countCollectedBetween(
                yesterdayStart,
                todayStart
        );

        long activeCompanyCount = dashboardQueryRepository.countActiveCompaniesAt(now);
        long yesterdayActiveCompanyCount = dashboardQueryRepository.countActiveCompaniesAt(
                now.minusDays(1)
        );

        MentionedTechResponse mostMentionedTech = dashboardQueryRepository.findMostMentionedTech(
                        now.minusDays(MENTION_PERIOD_DAYS),
                        now
                )
                .map(tech -> new MentionedTechResponse(tech.name(), tech.count()))
                .orElseGet(() -> new MentionedTechResponse(null, 0));

        RisingTechResponse mostRisingTech = dashboardQueryRepository.findMostRisingTech(
                        now.minusDays(RISING_PERIOD_DAYS * 2L),
                        now.minusDays(RISING_PERIOD_DAYS),
                        now,
                        MINIMUM_PREVIOUS_MENTIONS
                )
                .map(tech -> new RisingTechResponse(tech.name(), tech.rate()))
                .orElseGet(() -> new RisingTechResponse(null, 0));

        return new DashboardSummaryResponse(
                todayCollectedCount,
                todayCollectedCount - yesterdayCollectedCount,
                activeCompanyCount,
                activeCompanyCount - yesterdayActiveCompanyCount,
                mostMentionedTech,
                mostRisingTech
        );
    }
}
