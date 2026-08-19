package com.example.devs.domain.user.presentation.dto.response;

public record OAuthTokenResponse(
        String accessToken,
        String refreshToken,
        boolean onboardingRequired
) {
}
