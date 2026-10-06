package com.example.devs.domain.chat.domain.repository;

import com.example.devs.domain.chat.domain.ChatToolCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatToolCallRepository extends JpaRepository<ChatToolCall, Long> {
    @Query("""
            select toolCall
            from ChatToolCall toolCall
            where toolCall.message.session.id = :sessionId
            order by toolCall.id
            """)
    List<ChatToolCall> findBySessionId(@Param("sessionId") Integer sessionId);
}
