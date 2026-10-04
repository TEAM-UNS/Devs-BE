package com.example.devs.domain.user.presentation.dto.response;

import com.example.devs.domain.user.domain.PersonalHistory;

import java.util.List;

public record UserMyQueryResponse(
        String name,
        String email,
        PersonalHistory personalHistory,
        List<UserMajorResponse> majors,
        List<UserTechStackResponse> techStacks
) {
}
