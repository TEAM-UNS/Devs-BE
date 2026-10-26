package com.example.devs.domain.chat.domain.repository;

import com.example.devs.domain.chat.domain.ChatToolCall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query(value = """
            delete from chat.chat_tool_call
            where message_id in (
                select id
                from chat.chat_message
                where session_id = :sessionId
            )
            """, nativeQuery = true)
    void deleteBySessionId(@Param("sessionId") Integer sessionId);
}
