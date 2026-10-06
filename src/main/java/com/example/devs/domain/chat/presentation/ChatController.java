package com.example.devs.domain.chat.presentation;

import com.example.devs.domain.chat.presentation.dto.response.ChatMessageListResponse;
import com.example.devs.domain.chat.presentation.dto.response.ChatSessionListResponse;
import com.example.devs.domain.chat.service.ChatMessageListQueryService;
import com.example.devs.domain.chat.service.ChatSessionListQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatSessionListQueryService chatSessionListQueryService;
    private final ChatMessageListQueryService chatMessageListQueryService;

    @GetMapping("/sessions")
    public ChatSessionListResponse getSessions(
            @PageableDefault(size = 20, sort = "lastMessageAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return chatSessionListQueryService.execute(pageable);
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public ChatMessageListResponse getMessages(@PathVariable Integer sessionId) {
        return chatMessageListQueryService.execute(sessionId);
    }
}
