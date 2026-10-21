package com.example.devs.domain.report.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDate;

/**
 * AI 서버에 한 주차 리포트의 LLM 요약 생성을 요청한다.
 * AI 서버는 202를 바로 반환하고 생성은 비동기로 진행하며, 끝나면 /internal/reports/ready 로 통지한다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RequestLlmReportGenerationService {
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final WebClient aiWebClient;

    // 요청이 실패해도 AI 서버가 매시 정각에 비어 있는 리포트를 채우므로 예외를 던지지 않는다.
    public boolean execute(LocalDate weekStartDate, boolean force) {
        try {
            aiWebClient.post()
                    .uri("/api/reports/generate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(new GenerateRequest(weekStartDate.toString(), force))
                    .retrieve()
                    .toBodilessEntity()
                    .block(TIMEOUT);
            return true;
        } catch (Exception e) {
            log.error("LLM 리포트 생성 요청 실패. weekStartDate={}, force={}", weekStartDate, force, e);
            return false;
        }
    }

    // AI 서버는 snake_case 필드를 받는다.
    private record GenerateRequest(
            @JsonProperty("week_start_date") String weekStartDate,
            boolean force
    ) {}
}
