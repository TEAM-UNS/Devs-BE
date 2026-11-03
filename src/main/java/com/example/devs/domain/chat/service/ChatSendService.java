package com.example.devs.domain.chat.service;

import com.example.devs.domain.chat.domain.repository.ChatSessionRepository;
import com.example.devs.domain.chat.exception.ChatSessionNotFoundException;
import com.example.devs.domain.chat.presentation.dto.request.ChatSendRequest;
import com.example.devs.domain.user.facade.UserFacade;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatSendService {
    private static final ParameterizedTypeReference<ServerSentEvent<String>> SSE_TYPE =
            new ParameterizedTypeReference<>() {};
    private static final String DEFAULT_ERROR_MESSAGE = "AI 응답을 가져오지 못했습니다.";

    private final ChatSessionRepository chatSessionRepository;
    private final UserFacade userFacade;
    private final WebClient aiWebClient;
    private final ObjectMapper objectMapper;

    public Flux<ServerSentEvent<String>> execute(ChatSendRequest request) {

        Long userId = userFacade.getCurrentUserId();

        if (request.sessionId() != null) {
            chatSessionRepository.findByIdAndUserId(request.sessionId(), userId)
                    .orElseThrow(ChatSessionNotFoundException::new);
        }

        return aiWebClient.post()
                .uri("/api/chat/stream")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.TEXT_EVENT_STREAM)
                .bodyValue(new AiChatRequest(userId, request.sessionId(), request.message()))
                .retrieve()
                .bodyToFlux(SSE_TYPE)
                // 스트림이 시작되면 상태 코드를 바꿀 수 없으므로 에러를 이벤트로 내려준다.
                .onErrorResume(WebClientResponseException.class, exception -> {
                    if (exception.getStatusCode().is4xxClientError()) {
                        log.warn("AI 채팅 거절. status={}, userId={}, sessionId={}, body={}",
                                exception.getStatusCode(), userId, request.sessionId(),
                                exception.getResponseBodyAsString());
                    } else {
                        log.error("AI 채팅 실패. status={}, userId={}, sessionId={}, body={}",
                                exception.getStatusCode(), userId, request.sessionId(),
                                exception.getResponseBodyAsString());
                    }
                    return Flux.just(errorEvent(aiErrorData(exception)));
                })
                .onErrorResume(exception -> {
                    log.error("AI 채팅 스트리밍 실패. userId={}, sessionId={}", userId, request.sessionId(), exception);
                    return Flux.just(errorEvent(DEFAULT_ERROR_MESSAGE));
                });
    }

    private String aiErrorData(WebClientResponseException exception) {
        try {
            AiErrorResponse response = objectMapper.readValue(
                    exception.getResponseBodyAsString(), AiErrorResponse.class);
            if (response == null || response.error() == null || response.error().message() == null) {
                return DEFAULT_ERROR_MESSAGE;
            }
            return objectMapper.writeValueAsString(response.error());
        } catch (JacksonException e) {
            return DEFAULT_ERROR_MESSAGE;
        }
    }

    private static ServerSentEvent<String> errorEvent(String data) {
        return ServerSentEvent.<String>builder()
                .event("error")
                .data(data)
                .build();
    }

    // AI 서버는 snake_case 필드를 받는다. 전역 camelCase 설정 대신 필드명을 고정한다.
    private record AiChatRequest(
            @JsonProperty("user_id") Long userId,
            @JsonProperty("session_id") Integer sessionId,
            String message
    ) {}

    private record AiErrorResponse(AiError error) {}

    private record AiError(String code, String message, JsonNode detail) {}
}
