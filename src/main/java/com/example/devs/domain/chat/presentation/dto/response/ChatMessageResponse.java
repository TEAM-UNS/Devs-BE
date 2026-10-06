package com.example.devs.domain.chat.presentation.dto.response;

import com.example.devs.domain.chat.domain.ChatMessage;
import com.example.devs.domain.chat.domain.ChatToolCall;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder
public record ChatMessageResponse(
        Long id,
        String role,
        String content,
        List<ChatToolCallResponse> toolCalls,
        OffsetDateTime createdAt
) {
    public static ChatMessageResponse from(ChatMessage message, List<ChatToolCall> toolCalls) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .role(message.getRole())
                .content(message.getContent())
                .toolCalls(toolCalls.stream().map(ChatToolCallResponse::from).toList())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
