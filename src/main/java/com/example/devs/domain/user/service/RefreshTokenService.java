package com.example.devs.domain.user.service;

import com.example.devs.global.security.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private static final String REFRESH_TOKEN_KEY_PREFIX = "refresh-token:";

    private final StringRedisTemplate redisTemplate;
    private final JwtProperties jwtProperties;

    public void save(Long userId, String refreshToken) {
        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_KEY_PREFIX + userId,
                refreshToken,
                jwtProperties.refreshExpiration()
        );
    }

    public boolean matches(Long userId, String refreshToken) {
        String savedRefreshToken = redisTemplate.opsForValue()
                .get(REFRESH_TOKEN_KEY_PREFIX + userId);

        if (savedRefreshToken == null) {
            return false;
        }

        return MessageDigest.isEqual(
                savedRefreshToken.getBytes(StandardCharsets.UTF_8),
                refreshToken.getBytes(StandardCharsets.UTF_8)
        );
    }
}
