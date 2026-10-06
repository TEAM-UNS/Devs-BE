package com.example.devs.domain.chat.presentation.dto.response;

import com.example.devs.domain.chat.domain.ChatSession;

import java.util.List;

public record ChatMessageListResponse(
        Integer sessionId,
        String title,
        List<ChatMessageResponse> messages
) {
    public static ChatMessageListResponse from(ChatSession session, List<ChatMessageResponse> messages) {
        return new ChatMessageListResponse(session.getId(), session.getTitle(), messages);
    }
}
