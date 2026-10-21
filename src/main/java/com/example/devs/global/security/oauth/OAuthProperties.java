package com.example.devs.global.security.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "oauth")
public record OAuthProperties(
        URI frontendRedirectUri
) {
    public OAuthProperties {
        if (frontendRedirectUri == null) {
            throw new IllegalArgumentException("OAuth 프론트엔드 리디렉션 URI는 필수입니다.");
        }
    }
}
