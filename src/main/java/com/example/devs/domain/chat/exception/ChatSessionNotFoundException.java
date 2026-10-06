package com.example.devs.domain.chat.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class ChatSessionNotFoundException extends DevsException {
    public ChatSessionNotFoundException() {
        super(ErrorCode.CHAT_SESSION_NOT_FOUND);
    }
}
