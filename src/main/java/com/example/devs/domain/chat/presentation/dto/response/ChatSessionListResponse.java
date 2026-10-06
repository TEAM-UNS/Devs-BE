package com.example.devs.domain.chat.presentation.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record ChatSessionListResponse(
        List<ChatSessionResponse> sessions,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) {
    public static ChatSessionListResponse from(Page<ChatSessionResponse> sessions) {
        return new ChatSessionListResponse(
                sessions.getContent(),
                sessions.getNumber(),
                sessions.getSize(),
                sessions.getTotalElements(),
                sessions.getTotalPages(),
                sessions.hasNext()
        );
    }
}
