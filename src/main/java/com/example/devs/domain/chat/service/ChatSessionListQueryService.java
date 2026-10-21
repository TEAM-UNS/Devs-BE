package com.example.devs.domain.chat.service;

import com.example.devs.domain.chat.domain.repository.ChatSessionRepository;
import com.example.devs.domain.chat.presentation.dto.response.ChatSessionListResponse;
import com.example.devs.domain.chat.presentation.dto.response.ChatSessionResponse;
import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatSessionListQueryService {

    private static final Sort SESSION_SORT = Sort.by(Sort.Direction.DESC, "lastMessageAt", "id");

    private final ChatSessionRepository chatSessionRepository;
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public ChatSessionListResponse execute(int page, int size) {
        Long userId = userFacade.getCurrentUserId();
        Pageable pageable = PageRequest.of(page, size, SESSION_SORT);

        return ChatSessionListResponse.from(
                chatSessionRepository.findByUserId(userId, pageable).map(ChatSessionResponse::from)
        );
    }
}
