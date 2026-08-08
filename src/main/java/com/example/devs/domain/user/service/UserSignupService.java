package com.example.devs.domain.user.service;

import com.example.devs.domain.skill.domain.Skill;
import com.example.devs.domain.skill.domain.repository.SkillFieldRepository;
import com.example.devs.domain.skill.domain.repository.SkillRepository;
import com.example.devs.domain.skill.exception.SkillNotFoundException;
import com.example.devs.domain.skill.exception.SkillTechFieldMismatchException;
import com.example.devs.domain.tech_field.domain.TechField;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.exception.MajorNotFoundException;
import com.example.devs.domain.user.domain.User;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.EmailAlreadyExistsException;
import com.example.devs.domain.user.presentation.dto.request.UserSignupRequest;
import com.example.devs.domain.user.util.EmailNormalizer;
import com.example.devs.domain.user_skill.domain.UserSkill;
import com.example.devs.domain.user_skill.domain.repository.UserSkillRepository;
import com.example.devs.domain.user_major.domain.UserMajor;
import com.example.devs.domain.user_major.domain.repository.UserMajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSignupService {
    private final UserRepository userRepository;
    private final TechFieldRepository techFieldRepository;
    private final SkillRepository skillRepository;
    private final SkillFieldRepository skillFieldRepository;
    private final UserMajorRepository userMajorRepository;
    private final UserSkillRepository userSkillRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public void execute(UserSignupRequest userSignupRequest) {
        String email = EmailNormalizer.normalize(userSignupRequest.email());

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        emailVerificationService.validateVerified(email);

        List<TechField> majors = techFieldRepository.findAllById(userSignupRequest.majorIds());
        if (majors.size() != userSignupRequest.majorIds().size()) {
            throw new MajorNotFoundException();
        }

        List<Skill> skills = skillRepository.findAllById(userSignupRequest.skillIds());
        if (skills.size() != userSignupRequest.skillIds().size()) {
            throw new SkillNotFoundException();
        }

        long matchedSkillCount = skillFieldRepository.countMatchedSkills(
                userSignupRequest.skillIds(),
                userSignupRequest.majorIds()
        );
        if (matchedSkillCount != skills.size()) {
            throw new SkillTechFieldMismatchException();
        }

        User user = User.builder()
                .email(email)
                .name(userSignupRequest.name())
                .password(passwordEncoder.encode(userSignupRequest.password()))
                .personalHistory(userSignupRequest.personalHistory())
                .build();

        userRepository.save(user);

        List<UserMajor> userMajors = majors.stream()
                .map(major -> UserMajor.builder()
                        .user(user)
                        .major(major)
                        .build())
                .toList();

        List<UserSkill> userSkills = skills.stream()
                .map(skill -> UserSkill.builder()
                        .user(user)
                        .skill(skill)
                        .build())
                .toList();

        userMajorRepository.saveAll(userMajors);
        userSkillRepository.saveAll(userSkills);
        userRepository.flush();

        emailVerificationService.clearVerification(email);
    }
}
