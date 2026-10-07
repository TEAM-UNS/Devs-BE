package com.example.devs.domain.chat.presentation;

import com.example.devs.domain.chat.presentation.dto.request.ChatSendRequest;
import com.example.devs.domain.chat.presentation.dto.response.ChatMessageListResponse;
import com.example.devs.domain.chat.presentation.dto.response.ChatSessionListResponse;
import com.example.devs.domain.chat.service.ChatMessageListQueryService;
import com.example.devs.domain.chat.service.ChatSendService;
import com.example.devs.domain.chat.service.ChatSessionListQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatSessionListQueryService chatSessionListQueryService;
    private final ChatMessageListQueryService chatMessageListQueryService;
    private final ChatSendService chatSendService;

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

    @PostMapping(value = "/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> send(@Valid @RequestBody ChatSendRequest request) {
        return chatSendService.execute(request);
    }
}
