package com.example.devs.domain.chat.presentation.dto.response;

import com.example.devs.domain.chat.domain.ChatToolCall;

import java.util.Map;

public record ChatToolCallResponse(
        String toolName,
        Map<String, Object> chartPayload
) {
    public static ChatToolCallResponse from(ChatToolCall toolCall) {
        return new ChatToolCallResponse(toolCall.getToolName(), toolCall.getChartPayload());
    }
}
