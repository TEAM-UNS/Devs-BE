package com.example.devs.domain.user.service;

import com.example.devs.domain.tech_field.domain.TechField;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.exception.MajorNotFoundException;
import com.example.devs.domain.user.domain.User;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.UserNotFoundException;
import com.example.devs.domain.user.presentation.dto.request.UserMajorUpdateRequest;
import com.example.devs.domain.user_major.domain.UserMajor;
import com.example.devs.domain.user_major.domain.repository.UserMajorRepository;
import com.example.devs.domain.user_skill.domain.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMajorUpdateService {
    private final UserRepository userRepository;
    private final TechFieldRepository techFieldRepository;
    private final UserMajorRepository userMajorRepository;
    private final UserSkillRepository userSkillRepository;

    @Transactional
    public void execute(Long userId, UserMajorUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        List<TechField> majors = techFieldRepository.findAllById(request.majorIds());
        if (majors.size() != request.majorIds().size()) {
            throw new MajorNotFoundException();
        }

        user.updatePersonalHistory(request.personalHistory());

        userSkillRepository.deleteSkillsNotInMajors(
                userId,
                request.majorIds()
        );

        userMajorRepository.deleteAllByUserId(userId);

        List<UserMajor> updatedMajors = majors.stream()
                .map(major -> UserMajor.builder()
                        .user(user)
                        .major(major)
                        .build())
                .toList();

        userMajorRepository.saveAll(updatedMajors);
    }
}
