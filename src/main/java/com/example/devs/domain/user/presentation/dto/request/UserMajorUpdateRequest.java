package com.example.devs.domain.user.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record UserMajorUpdateRequest(
        @NotEmpty
        @NotNull
        Set<@Positive Integer> majorIds
) {
}
