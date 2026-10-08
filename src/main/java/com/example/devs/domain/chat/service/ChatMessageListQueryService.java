package com.example.devs.domain.chat.service;

import com.example.devs.domain.chat.domain.ChatSession;
import com.example.devs.domain.chat.domain.ChatToolCall;
import com.example.devs.domain.chat.domain.repository.ChatMessageRepository;
import com.example.devs.domain.chat.domain.repository.ChatSessionRepository;
import com.example.devs.domain.chat.domain.repository.ChatToolCallRepository;
import com.example.devs.domain.chat.exception.ChatSessionNotFoundException;
import com.example.devs.domain.chat.presentation.dto.response.ChatMessageListResponse;
import com.example.devs.domain.chat.presentation.dto.response.ChatMessageResponse;
import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageListQueryService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatToolCallRepository chatToolCallRepository;
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public ChatMessageListResponse execute(Integer sessionId) {
        Long userId = userFacade.getCurrentUserId();
        // 다른 유저의 세션은 존재 여부를 노출하지 않도록 404로 응답한다.
        ChatSession session = chatSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(ChatSessionNotFoundException::new);

        Map<Long, List<ChatToolCall>> toolCallsByMessageId = chatToolCallRepository.findBySessionId(sessionId)
                .stream()
                .collect(Collectors.groupingBy(toolCall -> toolCall.getMessage().getId()));

        List<ChatMessageResponse> messages = chatMessageRepository.findBySessionIdOrderByCreatedAtAscIdAsc(sessionId)
                .stream()
                .map(ChatMessageResponse::from)
                .toList();

        return ChatMessageListResponse.from(session, messages);
    }
}
