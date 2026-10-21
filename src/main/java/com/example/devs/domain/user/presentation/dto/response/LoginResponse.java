package com.example.devs.domain.user.presentation.dto.response;

import java.util.List;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        List<UserMajorResponse> majors
) {
    public static LoginResponse of(
            String accessToken,
            String refreshToken,
            List<UserMajorResponse> majors
    ) {
        return new LoginResponse(
                accessToken,
                refreshToken,
                majors
        );
    }
}
