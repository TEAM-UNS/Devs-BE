package com.example.devs.domain.roadmap.presentation.dto.request;

import jakarta.validation.constraints.Size;

public record RoadmapRetrospectRequest(
        @Size(max = 1000, message = "회고는 1000자 이하로 작성해야 합니다.")
        String retrospect
) {
}
