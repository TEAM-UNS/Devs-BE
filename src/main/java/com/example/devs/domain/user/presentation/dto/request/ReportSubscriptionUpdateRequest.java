package com.example.devs.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record ReportSubscriptionUpdateRequest(
        @NotNull Boolean totalReport,
        @NotNull Boolean majorReport,
        @NotNull Set<@Positive Integer> majorIds
) {
}