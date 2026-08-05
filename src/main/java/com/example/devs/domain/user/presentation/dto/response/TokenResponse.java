package com.example.devs.domain.user.presentation.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
