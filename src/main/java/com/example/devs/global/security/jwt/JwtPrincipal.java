package com.example.devs.global.security.jwt;

public record JwtPrincipal(
        Long userId,
        String email
) {
}
