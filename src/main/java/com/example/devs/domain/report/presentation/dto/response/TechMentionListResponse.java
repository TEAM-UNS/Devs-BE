package com.example.devs.domain.report.presentation.dto.response;

import java.util.List;

public record TechMentionListResponse(
        List<TechMentionResponse> mentions
) {
    public static TechMentionListResponse from(List<TechMentionResponse> mentions) {
        return new TechMentionListResponse(mentions);
    }
}
