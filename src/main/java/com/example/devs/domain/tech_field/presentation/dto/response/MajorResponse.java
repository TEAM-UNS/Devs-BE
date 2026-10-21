package com.example.devs.domain.tech_field.presentation.dto.response;

import java.util.List;

public record MajorResponse(
        Integer id,
        String major,
        List<TechStackResponse> techStacks
) {
    public static MajorResponse of(Integer id, String major, List<TechStackResponse> techStacks) {
        return new MajorResponse(id, major, techStacks);
    }
}
