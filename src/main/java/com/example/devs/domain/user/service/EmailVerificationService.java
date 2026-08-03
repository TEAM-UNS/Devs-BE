package com.example.devs.domain.user.service;

import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.EmailAlreadyExistsException;
import com.example.devs.domain.user.exception.EmailNotVerifiedException;
import com.example.devs.domain.user.exception.EmailSendFailedException;
import com.example.devs.domain.user.exception.EmailVerificationCodeExpiredException;
import com.example.devs.domain.user.exception.EmailVerificationCodeMismatchException;
import com.example.devs.domain.user.exception.EmailVerificationRequestLimitException;
import com.example.devs.domain.user.presentation.dto.request.EmailVerificationConfirmRequest;
import com.example.devs.domain.user.presentation.dto.request.EmailVerificationSendRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Locale;

@Service
public class EmailVerificationService {
    private static final String CODE_KEY_PREFIX = "email-verification:code:";
    private static final String VERIFIED_KEY_PREFIX = "email-verification:verified:";
    private static final String COOLDOWN_KEY_PREFIX = "email-verification:cooldown:";
    private static final Duration CODE_EXPIRATION = Duration.ofMinutes(5);
    private static final Duration VERIFIED_EXPIRATION = Duration.ofMinutes(30);
    private static final Duration RESEND_COOLDOWN = Duration.ofMinutes(5);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private final String senderEmail;

    public EmailVerificationService(
            UserRepository userRepository,
            StringRedisTemplate redisTemplate,
            JavaMailSender mailSender,
            @Value("${spring.mail.username}") String senderEmail
    ) {
        this.userRepository = userRepository;
        this.redisTemplate = redisTemplate;
        this.mailSender = mailSender;
        this.senderEmail = senderEmail;
    }

    public void sendVerificationCode(EmailVerificationSendRequest request) {
        String email = normalizeEmail(request.email());

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }

        String cooldownKey = COOLDOWN_KEY_PREFIX + email;
        Boolean requestAccepted = redisTemplate.opsForValue()
                .setIfAbsent(cooldownKey, "true", RESEND_COOLDOWN);

        if (!Boolean.TRUE.equals(requestAccepted)) {
            throw new EmailVerificationRequestLimitException();
        }

        String codeKey = CODE_KEY_PREFIX + email;
        String code = generateCode();
        redisTemplate.delete(VERIFIED_KEY_PREFIX + email);
        redisTemplate.opsForValue().set(codeKey, code, CODE_EXPIRATION);

        try {
            mailSender.send(createVerificationMail(email, code));
        } catch (MailException exception) {
            redisTemplate.delete(codeKey);
            redisTemplate.delete(cooldownKey);
            throw new EmailSendFailedException();
        }
    }

    public void verifyCode(EmailVerificationConfirmRequest request) {
        String email = normalizeEmail(request.email());
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
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(VERIFIED_KEY_PREFIX + normalizeEmail(email)))) {
            throw new EmailNotVerifiedException();
        }
    }

    public void clearVerification(String email) {
        String normalizedEmail = normalizeEmail(email);
        redisTemplate.delete(VERIFIED_KEY_PREFIX + normalizedEmail);
        redisTemplate.delete(COOLDOWN_KEY_PREFIX + normalizedEmail);
    }

    private String generateCode() {
        return String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
    }

    private SimpleMailMessage createVerificationMail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(email);
        message.setSubject("[Devs] 이메일 인증 코드");
        message.setText("이메일 인증 코드는 " + code + "입니다. 5분 안에 입력해 주세요.");
        return message;
    }

    private boolean codesMatch(String savedCode, String inputCode) {
        return MessageDigest.isEqual(
                savedCode.getBytes(StandardCharsets.UTF_8),
                inputCode.getBytes(StandardCharsets.UTF_8)
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
