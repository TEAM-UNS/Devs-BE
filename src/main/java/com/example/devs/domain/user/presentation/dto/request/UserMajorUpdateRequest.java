package com.example.devs.domain.user.presentation.dto.request;

import com.example.devs.domain.user.domain.PersonalHistory;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record UserMajorUpdateRequest(
        @NotNull(message = "경력은 필수입니다.")
        PersonalHistory personalHistory,

        @NotEmpty
        @NotNull
        Set<@Positive Integer> majorIds
) {
}
