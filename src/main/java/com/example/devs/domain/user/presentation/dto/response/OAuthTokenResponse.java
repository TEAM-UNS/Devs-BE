package com.example.devs.domain.user.presentation.dto.response;

import java.util.List;

public record OAuthTokenResponse(
        String accessToken,
        String refreshToken,
        boolean onboardingRequired,
        List<UserMajorResponse> majors
) {
}
