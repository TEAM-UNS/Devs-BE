package com.example.devs.domain.user.service;

import com.example.devs.domain.skill.domain.Skill;
import com.example.devs.domain.skill.domain.repository.SkillFieldRepository;
import com.example.devs.domain.skill.domain.repository.SkillRepository;
import com.example.devs.domain.skill.exception.SkillNotFoundException;
import com.example.devs.domain.skill.exception.SkillTechFieldMismatchException;
import com.example.devs.domain.user.domain.User;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.UserNotFoundException;
import com.example.devs.domain.user.presentation.dto.request.UserTechStackUpdateRequest;
import com.example.devs.domain.user_major.domain.repository.UserMajorRepository;
import com.example.devs.domain.user_skill.domain.UserSkill;
import com.example.devs.domain.user_skill.domain.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserTechStackUpdateService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final SkillFieldRepository skillFieldRepository;
    private final UserMajorRepository userMajorRepository;
    private final UserSkillRepository userSkillRepository;

    @Transactional
    public void execute(Long userId, UserTechStackUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        List<Skill> skills = skillRepository.findAllById(request.skillIds());
        if (skills.size() != request.skillIds().size()) {
            throw new SkillNotFoundException();
        }

        List<Integer> majorIds = userMajorRepository.findMajorIdsByUserId(userId);
        if (majorIds.isEmpty()) {
            throw new SkillTechFieldMismatchException();
        }

        long matchedSkillCount = skillFieldRepository.countMatchedSkills(
                request.skillIds(),
                majorIds
        );
        if (matchedSkillCount != skills.size()) {
            throw new SkillTechFieldMismatchException();
        }

        userSkillRepository.deleteAllByUserId(userId);

        List<UserSkill> updatedSkills = skills.stream()
                .map(skill -> UserSkill.builder()
                        .user(user)
                        .skill(skill)
                        .build())
                .toList();

        userSkillRepository.saveAll(updatedSkills);
    }
}
