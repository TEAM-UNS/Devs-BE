package com.example.devs.domain.dashboard.presentation.dto.response;

public record MentionedTechResponse(
        String name,
        long count
) {
    public static MentionedTechResponse of(String name, long count) {
        return new MentionedTechResponse(name, count);
    }
}
