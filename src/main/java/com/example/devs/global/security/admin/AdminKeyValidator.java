package com.example.devs.global.security.admin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

/** 관리자 전용 API 요청의 키를 검증한다. 서버에 키가 설정되지 않았으면 모든 요청을 거부한다. */
@Component
public class AdminKeyValidator {
    private final byte[] adminKey;

    public AdminKeyValidator(@Value("${admin.key:}") String adminKey) {
        this.adminKey = adminKey.getBytes(StandardCharsets.UTF_8);
    }

    public void validate(String requestKey) {
        if (adminKey.length == 0 || requestKey == null
                || !MessageDigest.isEqual(adminKey, requestKey.getBytes(StandardCharsets.UTF_8))) {
            throw new InvalidAdminKeyException();
        }
    }
}
