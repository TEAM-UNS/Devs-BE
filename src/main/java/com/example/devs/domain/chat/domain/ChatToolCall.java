package com.example.devs.domain.chat.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Entity
@Immutable
@Table(name = "chat_tool_call", schema = "chat")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatToolCall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private ChatMessage message;

    @Column(name = "tool_name", nullable = false, length = 64)
    private String toolName;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "chart_payload", columnDefinition = "jsonb")
    private Map<String, Object> chartPayload;
}
