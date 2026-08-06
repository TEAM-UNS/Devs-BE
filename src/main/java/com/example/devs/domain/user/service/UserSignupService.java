package com.example.devs.domain.user.service;

import com.example.devs.domain.major.domain.Major;
import com.example.devs.domain.major.domain.repository.MajorRepository;
import com.example.devs.domain.major.exception.MajorNotFoundException;
import com.example.devs.domain.tech_stack.domain.TechStack;
import com.example.devs.domain.tech_stack.domain.repository.TechStackRepository;
import com.example.devs.domain.tech_stack.exception.TechStackMajorMismatchException;
import com.example.devs.domain.tech_stack.exception.TechStackNotFoundException;
import com.example.devs.domain.user.domain.User;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.EmailAlreadyExistsException;
import com.example.devs.domain.user.presentation.dto.request.UserSignupRequest;
import com.example.devs.domain.user.util.EmailNormalizer;
import com.example.devs.domain.user_major.domain.UserMajor;
import com.example.devs.domain.user_major.domain.repository.UserMajorRepository;
import com.example.devs.domain.user_tech_stack.domain.UserTechStack;
import com.example.devs.domain.user_tech_stack.domain.repository.UserTechStackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSignupService {
    private final UserRepository userRepository;
    private final MajorRepository majorRepository;
    private final TechStackRepository techStackRepository;
    private final UserMajorRepository userMajorRepository;
    private final UserTechStackRepository userTechStackRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public void execute(UserSignupRequest userSignupRequest) {
        String email = EmailNormalizer.normalize(userSignupRequest.email());

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        emailVerificationService.validateVerified(email);

        List<Major> majors = majorRepository.findAllById(userSignupRequest.majorIds());
        if (majors.size() != userSignupRequest.majorIds().size()) {
            throw new MajorNotFoundException();
        }

        List<TechStack> techStacks = techStackRepository.findAllById(
                userSignupRequest.techStackIds()
        );
        if (techStacks.size() != userSignupRequest.techStackIds().size()) {
            throw new TechStackNotFoundException();
        }

        boolean hasMismatchedTechStack = techStacks.stream()
                .anyMatch(techStack -> !userSignupRequest.majorIds()
                        .contains(techStack.getMajor().getId()));

        if (hasMismatchedTechStack) {
            throw new TechStackMajorMismatchException();
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

        List<UserTechStack> userTechStacks = techStacks.stream()
                .map(techStack -> UserTechStack.builder()
                        .user(user)
                        .techStack(techStack)
                        .build())
                .toList();

        userMajorRepository.saveAll(userMajors);
        userTechStackRepository.saveAll(userTechStacks);
        userRepository.flush();

        emailVerificationService.clearVerification(email);
    }
}
