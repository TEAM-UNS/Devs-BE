package com.example.devs.domain.user.service;

import com.example.devs.domain.user.domain.PersonalHistory;
import com.example.devs.domain.user.domain.User;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.InvalidOAuthProfileException;
import com.example.devs.domain.user.presentation.dto.response.OAuthTokenResponse;
import com.example.devs.domain.user.util.EmailNormalizer;
import com.example.devs.domain.user_major.domain.repository.UserMajorRepository;
import com.example.devs.domain.user_skill.domain.repository.UserSkillRepository;
import com.example.devs.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.example.devs.domain.user.domain.User.normalizeEmail;
import static com.example.devs.domain.user.domain.User.resolveName;

@Service
@RequiredArgsConstructor
public class GoogleOAuthLoginService {
    private final UserRepository userRepository;
    private final UserMajorRepository userMajorRepository;
    private final UserSkillRepository userSkillRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public OAuthTokenResponse execute(OidcUser oidcUser) {
        if (oidcUser == null || !Boolean.TRUE.equals(oidcUser.getEmailVerified())) {
            throw new InvalidOAuthProfileException();
        }

        String email = normalizeEmail(oidcUser.getClaimAsString("email"));
        String name = resolveName(oidcUser.getClaimAsString("name"), email);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createUser(email, name));

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);
        refreshTokenService.save(user.getId(), refreshToken);

        boolean onboardingRequired = !userMajorRepository.existsByUserId(user.getId())
                || !userSkillRepository.existsByUserId(user.getId());

        return new OAuthTokenResponse(
                accessToken,
                refreshToken,
                onboardingRequired
        );
    }

    private User createUser(String email, String name) {
        User user = User.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                .personalHistory(PersonalHistory.NO_EXPERIENCE)
                .build();

        return userRepository.save(user);
    }
}
