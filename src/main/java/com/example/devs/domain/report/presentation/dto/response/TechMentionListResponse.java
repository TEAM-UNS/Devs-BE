package com.example.devs.domain.report.presentation.dto.response;

import java.util.List;

public record TechMentionListResponse(
        List<TechMentionResponse> mentions
) {
}
