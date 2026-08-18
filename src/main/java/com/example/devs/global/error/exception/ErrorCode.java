package com.example.devs.global.error.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_LOGIN_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
    EMAIL_VERIFICATION_REQUEST_LIMIT(
            HttpStatus.TOO_MANY_REQUESTS,
            "인증 코드는 5분 후에 다시 요청할 수 있습니다."
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
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증 이메일 발송에 실패했습니다."),
    MAJOR_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 전공이 포함되어 있습니다."),
    SKILL_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 기술이 포함되어 있습니다."),
    SKILL_TECH_FIELD_MISMATCH(
            HttpStatus.BAD_REQUEST,
            "선택한 전공에 속하지 않는 기술이 포함되어 있습니다."
    );

    private final HttpStatus status;
    private final String message;
}
