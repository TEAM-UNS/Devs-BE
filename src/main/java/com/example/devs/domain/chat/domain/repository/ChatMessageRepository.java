package com.example.devs.domain.chat.domain.repository;

import com.example.devs.domain.chat.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySessionIdOrderByCreatedAtAscIdAsc(Integer sessionId);

    @Modifying
    @Query(value = """
            delete from chat.chat_message
            where session_id = :sessionId
            """, nativeQuery = true)
    void deleteBySessionId(@Param("sessionId") Integer sessionId);
}
