package com.example.devs.domain.report.domain;

public record WeeklyReportMail(
        String subject,
        String html,
        String text
) {
}
