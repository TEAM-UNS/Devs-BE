package com.example.devs.domain.chat.domain.repository;

import com.example.devs.domain.chat.domain.ChatSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Integer> {
    Page<ChatSession> findByUserId(Long userId, Pageable pageable);

    Optional<ChatSession> findByIdAndUserId(Integer id, Long userId);
}
