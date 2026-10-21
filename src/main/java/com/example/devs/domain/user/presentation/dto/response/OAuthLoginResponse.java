package com.example.devs.domain.user.presentation.dto.response;

import java.util.List;

public record OAuthLoginResponse(
        String accessToken,
        String refreshToken,
        boolean onboardingRequired,
        List<UserMajorResponse> majors
) {
    public static OAuthLoginResponse of(
            String accessToken,
            String refreshToken,
            boolean onboardingRequired,
            List<UserMajorResponse> majors
    ) {
        return new OAuthLoginResponse(
                accessToken,
                refreshToken,
                onboardingRequired,
                majors
        );
    }
}
