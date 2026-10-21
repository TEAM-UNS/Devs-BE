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
        String content
) {
    public static ChatMessageResponse from(ChatMessage message) {
        return ChatMessageResponse.builder()
                .id(message.getId())
                .role(message.getRole())
                .content(message.getContent())
                .build();
    }
}
