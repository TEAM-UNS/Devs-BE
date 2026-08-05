package com.example.devs.domain.user.service;

import com.example.devs.domain.user.domain.PersonalHistory;
import com.example.devs.domain.user.domain.User;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.EmailAlreadyExistsException;
import com.example.devs.domain.user.presentation.dto.request.UserSignupRequest;
import com.example.devs.domain.user.util.EmailNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSignupService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public void execute(UserSignupRequest userSignupRequest) {
        String email = EmailNormalizer.normalize(userSignupRequest.email());

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        emailVerificationService.validateVerified(email);

        User user = User.builder()
                .email(email)
                .name(userSignupRequest.name())
                .password(passwordEncoder.encode(userSignupRequest.password()))
                .personalHistory(PersonalHistory.ENTRY_LEVEL)
                .build();

        userRepository.saveAndFlush(user);
        emailVerificationService.clearVerification(email);
    }
}
