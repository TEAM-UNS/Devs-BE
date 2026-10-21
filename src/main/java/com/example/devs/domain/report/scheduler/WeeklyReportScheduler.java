package com.example.devs.domain.report.scheduler;

import com.example.devs.domain.report.service.CreateWeeklyReportsService;
import com.example.devs.domain.report.service.SendWeeklyReportMailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;

/** 매주 월요일에 직전 주의 통계를 전체 및 전공별 리포트로 적재하고 AI 서버에 LLM 요약 생성을 요청한다. */
@Component
@RequiredArgsConstructor
public class WeeklyReportScheduler {
    private final CreateWeeklyReportsService createWeeklyReportsService;
    private final SendWeeklyReportMailsService sendWeeklyReportMailsService;
    private final Clock clock;

    @Scheduled(cron = "0 10 0 * * MON", zone = "Asia/Seoul")
    public void createLastWeekReports() {
        createWeeklyReportsService.execute(LocalDate.now(clock).minusWeeks(1), false);
    }

    @Scheduled(cron = "0 0 9 * * MON", zone = "Asia/Seoul")
    public void sendLastWeekReportMails() {
        sendWeeklyReportMailsService.execute(LocalDate.now(clock).minusWeeks(1));
    }
}
