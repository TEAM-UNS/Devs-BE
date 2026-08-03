package com.example.devs.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    EMAIL_VERIFICATION_REQUEST_LIMIT(
            HttpStatus.TOO_MANY_REQUESTS,
            "인증 코드는 1분 후에 다시 요청할 수 있습니다."
    ),
    EMAIL_VERIFICATION_CODE_EXPIRED(
            HttpStatus.BAD_REQUEST,
            "인증 코드가 만료되었거나 존재하지 않습니다."
    ),
    EMAIL_VERIFICATION_CODE_MISMATCH(
            HttpStatus.BAD_REQUEST,
            "이메일 인증 코드가 일치하지 않습니다."
    ),
    EMAIL_NOT_VERIFIED(HttpStatus.FORBIDDEN, "이메일 인증이 필요합니다."),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증 이메일 발송에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}
