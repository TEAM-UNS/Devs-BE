package com.example.devs.domain.user.service;

import com.example.devs.domain.user.exception.EmailNotVerifiedException;
import com.example.devs.domain.user.exception.EmailVerificationCodeExpiredException;
import com.example.devs.domain.user.exception.EmailVerificationCodeMismatchException;
import com.example.devs.domain.user.presentation.dto.request.EmailVerificationConfirmRequest;
import com.example.devs.domain.user.util.EmailNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {
    private static final String CODE_KEY_PREFIX = "email-verification:code:";
    private static final String VERIFIED_KEY_PREFIX = "email-verification:verified:";
    private static final String COOLDOWN_KEY_PREFIX = "email-verification:cooldown:";
    private static final Duration VERIFIED_EXPIRATION = Duration.ofMinutes(30);

    private final StringRedisTemplate redisTemplate;

    public void execute(EmailVerificationConfirmRequest request) {
        String email = EmailNormalizer.normalize(request.email());
        String codeKey = CODE_KEY_PREFIX + email;
        String savedCode = redisTemplate.opsForValue().get(codeKey);

        if (savedCode == null) {
            throw new EmailVerificationCodeExpiredException();
        }

        if (!codesMatch(savedCode, request.code())) {
            throw new EmailVerificationCodeMismatchException();
        }

        redisTemplate.delete(codeKey);
        redisTemplate.opsForValue().set(
                VERIFIED_KEY_PREFIX + email,
                "true",
                VERIFIED_EXPIRATION
        );
    }

    public void validateVerified(String email) {
        if (!Boolean.TRUE.equals(
                redisTemplate.hasKey(VERIFIED_KEY_PREFIX + EmailNormalizer.normalize(email))
        )) {
            throw new EmailNotVerifiedException();
        }
    }

    public void clearVerification(String email) {
        String normalizedEmail = EmailNormalizer.normalize(email);
        redisTemplate.delete(VERIFIED_KEY_PREFIX + normalizedEmail);
        redisTemplate.delete(COOLDOWN_KEY_PREFIX + normalizedEmail);
    }

    private boolean codesMatch(String savedCode, String inputCode) {
        return MessageDigest.isEqual(
                savedCode.getBytes(StandardCharsets.UTF_8),
                inputCode.getBytes(StandardCharsets.UTF_8)
        );
    }
}
