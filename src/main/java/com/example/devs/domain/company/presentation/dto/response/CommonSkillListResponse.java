package com.example.devs.domain.company.presentation.dto.response;

import java.util.List;

public record CommonSkillListResponse(List<CommonSkillResponse> skills) {
    public static CommonSkillListResponse from(List<CommonSkillResponse> skills) {
        return new CommonSkillListResponse(skills);
    }
}
