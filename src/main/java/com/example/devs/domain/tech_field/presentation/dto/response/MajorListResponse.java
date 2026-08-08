package com.example.devs.domain.tech_field.presentation.dto.response;

import java.util.List;

public record MajorListResponse(
        List<MajorResponse> categories
) {
}
