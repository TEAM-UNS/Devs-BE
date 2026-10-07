package com.example.devs.global.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(AiProperties.class)
public class AiClientConfig {

    @Bean
    public WebClient aiWebClient(AiProperties aiProperties) {
        return WebClient.builder()
                .baseUrl(aiProperties.baseUrl().toString())
                .build();
    }
}
