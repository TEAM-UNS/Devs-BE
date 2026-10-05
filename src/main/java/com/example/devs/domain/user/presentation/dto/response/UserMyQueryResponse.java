package com.example.devs.domain.user.presentation.dto.response;

import com.example.devs.domain.skill.domain.Skill;
import com.example.devs.domain.tech_field.domain.TechField;
import com.example.devs.domain.user.domain.PersonalHistory;
import com.example.devs.domain.user.domain.User;

import java.util.List;

public record UserMyQueryResponse(
        String name,
        String email,
        PersonalHistory personalHistory,
        List<UserMajorResponse> majors,
        List<UserTechStackResponse> techStacks
) {
    public static UserMyQueryResponse from(User user, List<TechField> majors, List<Skill> techStacks) {
        return new UserMyQueryResponse(
                user.getName(),
                user.getEmail(),
                user.getPersonalHistory(),
                majors.stream().map(UserMajorResponse::from).toList(),
                techStacks.stream().map(UserTechStackResponse::from).toList()
        );
    }
}
