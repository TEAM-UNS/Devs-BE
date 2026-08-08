package com.example.devs.domain.user.service;

import com.example.devs.domain.user.domain.User;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.InvalidRefreshTokenException;
import com.example.devs.domain.user.presentation.dto.response.AccessTokenResponse;
import com.example.devs.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenReissueService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public AccessTokenResponse execute(String refreshToken) {
        Long userId = parseUserId(refreshToken);

        if (!refreshTokenService.matches(userId, refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(InvalidRefreshTokenException::new);

        return new AccessTokenResponse(jwtProvider.generateAccessToken(user));
    }

    private Long parseUserId(String refreshToken) {
        try {
            Jwt jwt = jwtProvider.parseRefreshToken(refreshToken);
            return Long.valueOf(jwt.getSubject());
        } catch (JwtException | NumberFormatException exception) {
            throw new InvalidRefreshTokenException();
        }
    }
}
