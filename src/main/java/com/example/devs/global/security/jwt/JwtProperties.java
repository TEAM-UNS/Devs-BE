package com.example.devs.global.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        Duration accessExpiration,
        Duration refreshExpiration
) {
    private static final int MINIMUM_SECRET_LENGTH = 32;

    public JwtProperties {
        if (secret == null
                || secret.getBytes(StandardCharsets.UTF_8).length < MINIMUM_SECRET_LENGTH) {
            throw new IllegalArgumentException("JWT_SECRET은 32바이트 이상이어야 합니다.");
        }
        if (accessExpiration == null || accessExpiration.isNegative() || accessExpiration.isZero()) {
            throw new IllegalArgumentException("Access Token 만료 시간은 0보다 커야 합니다.");
        }
        if (refreshExpiration == null
                || refreshExpiration.isNegative()
                || refreshExpiration.isZero()) {
            throw new IllegalArgumentException("Refresh Token 만료 시간은 0보다 커야 합니다.");
        }
    }
}
