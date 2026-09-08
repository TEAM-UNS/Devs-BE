package com.example.devs.domain.user.presentation.dto.response;

import java.util.List;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        List<UserMajorResponse> majors
) {
}
