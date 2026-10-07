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
import reactor.core.publisher.Flux;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatSendService {
    private static final ParameterizedTypeReference<ServerSentEvent<String>> SSE_TYPE =
            new ParameterizedTypeReference<>() {};

    private final ChatSessionRepository chatSessionRepository;
    private final UserFacade userFacade;
    private final WebClient aiWebClient;

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
                .onErrorResume(exception -> {
                    log.error("AI 채팅 스트리밍 실패. userId={}, sessionId={}", userId, request.sessionId(), exception);
                    return Flux.just(ServerSentEvent.<String>builder()
                            .event("error")
                            .data("AI 응답을 가져오지 못했습니다.")
                            .build());
                });
    }

    // AI 서버는 snake_case 필드를 받는다. 전역 camelCase 설정 대신 필드명을 고정한다.
    private record AiChatRequest(
            @JsonProperty("user_id") Long userId,
            @JsonProperty("session_id") Integer sessionId,
            String message
    ) {}
}
