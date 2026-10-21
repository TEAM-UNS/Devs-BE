package com.example.devs.domain.chat.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChatSendRequest(
        // null이면 AI 서버가 새 세션을 만든다.
        Integer sessionId,
        @NotBlank @Size(max = 4000) String message
) {}
