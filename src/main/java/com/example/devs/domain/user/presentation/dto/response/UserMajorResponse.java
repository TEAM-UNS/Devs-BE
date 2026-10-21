package com.example.devs.domain.user.presentation.dto.response;

import com.example.devs.domain.tech_field.domain.TechField;

public record UserMajorResponse(
        Integer majorId,
        String majorName
) {
    public static UserMajorResponse from(TechField major) {
        return new UserMajorResponse(major.getId(), major.getName());
    }
}
