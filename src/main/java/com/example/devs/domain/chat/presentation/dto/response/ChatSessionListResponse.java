package com.example.devs.domain.chat.presentation.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record ChatSessionListResponse(
        List<ChatSessionResponse> sessions
) {
    public static ChatSessionListResponse from(Page<ChatSessionResponse> sessions) {
        return new ChatSessionListResponse(
                sessions.getContent()
        );
    }
}
