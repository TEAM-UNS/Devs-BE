package com.example.devs.domain.roadmap.presentation.dto.request;

import jakarta.validation.constraints.NotNull;

public record RoadmapItemCheckRequest(
        @NotNull(message = "완료 여부를 입력해야 합니다.")
        Boolean done
) {
}
