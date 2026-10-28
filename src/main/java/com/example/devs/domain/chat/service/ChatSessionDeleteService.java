package com.example.devs.domain.chat.service;

import com.example.devs.domain.chat.domain.ChatSession;
import com.example.devs.domain.chat.domain.repository.ChatMessageRepository;
import com.example.devs.domain.chat.domain.repository.ChatSessionRepository;
import com.example.devs.domain.chat.domain.repository.ChatToolCallRepository;
import com.example.devs.domain.chat.exception.ChatSessionNotFoundException;
import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatSessionDeleteService {

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatToolCallRepository chatToolCallRepository;
    private final UserFacade userFacade;

    @Transactional
    public void execute(Integer sessionId) {
        Long userId = userFacade.getCurrentUserId();
        ChatSession session = chatSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(ChatSessionNotFoundException::new);

        chatToolCallRepository.deleteBySessionId(sessionId);
        chatMessageRepository.deleteBySessionId(sessionId);
        chatSessionRepository.delete(session);
    }
}
