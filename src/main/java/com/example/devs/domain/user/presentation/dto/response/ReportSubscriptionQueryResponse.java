package com.example.devs.domain.user.presentation.dto.response;

import com.example.devs.domain.user.domain.User;

import java.util.List;

public record ReportSubscriptionQueryResponse(
        boolean totalReport,
        boolean majorReport,
        List<Integer> majorIds
) {
    public static ReportSubscriptionQueryResponse from(User user) {
        return new ReportSubscriptionQueryResponse(
                user.isTotalReportSubscribed(),
                user.isMajorReportSubscribed(),
                user.getReportMajorIds().stream().sorted().toList()
        );
    }
}
