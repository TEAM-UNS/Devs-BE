package com.example.devs.domain.tech_field.presentation.dto.response;

import java.util.List;

public record MajorResponse(
        Integer id,
        String major,
        List<TechStackResponse> techStacks
) {
}
