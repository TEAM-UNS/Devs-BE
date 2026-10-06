package com.example.devs.domain.chat.presentation.dto.response;

import com.example.devs.domain.chat.domain.ChatSession;
import lombok.Builder;

import java.time.OffsetDateTime;

@Builder
public record ChatSessionResponse(
        Integer id,
        String title,
        OffsetDateTime lastMessageAt,
        OffsetDateTime createdAt
) {
    public static ChatSessionResponse from(ChatSession session) {
        return ChatSessionResponse.builder()
                .id(session.getId())
                .title(session.getTitle())
                .lastMessageAt(session.getLastMessageAt())
                .createdAt(session.getCreatedAt())
                .build();
    }
}
