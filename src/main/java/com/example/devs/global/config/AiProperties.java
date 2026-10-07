package com.example.devs.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.URI;

@ConfigurationProperties(prefix = "ai")
public record AiProperties(
        URI baseUrl
) {
    public AiProperties {
        if (baseUrl == null) {
            throw new IllegalArgumentException("AI 서버 주소(ai.base-url)는 필수입니다.");
        }
    }
}
