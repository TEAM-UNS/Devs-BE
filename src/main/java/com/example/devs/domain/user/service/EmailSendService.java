package com.example.devs.domain.user.service;

import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.EmailAlreadyExistsException;
import com.example.devs.domain.user.exception.EmailSendFailedException;
import com.example.devs.domain.user.exception.EmailVerificationRequestLimitException;
import com.example.devs.domain.user.presentation.dto.request.EmailVerificationSendRequest;
import com.example.devs.domain.user.util.EmailNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class EmailSendService {
    private static final String CODE_KEY_PREFIX = "email-verification:code:";
    private static final String VERIFIED_KEY_PREFIX = "email-verification:verified:";
    private static final String COOLDOWN_KEY_PREFIX = "email-verification:cooldown:";
    private static final Duration CODE_EXPIRATION = Duration.ofMinutes(5);
    private static final Duration RESEND_COOLDOWN = Duration.ofMinutes(5);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String TEMPLATE_PATH = "templates/mail/email-verification.html";
    private static final ClassPathResource LOGO = new ClassPathResource("templates/mail/devs-logo.png");
    private static final String TEMPLATE = loadTemplate();

    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String emailSender;

    public void execute(EmailVerificationSendRequest request) {
        String email = EmailNormalizer.normalize(request.email());

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
            mailSender.send(createMessage(email, code));
        } catch (MailException exception) {
            redisTemplate.delete(codeKey);
            redisTemplate.delete(cooldownKey);
            throw new EmailSendFailedException();
        }
    }

    private MimeMessagePreparator createMessage(String email, String code) {
        return mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(emailSender);
            helper.setTo(email);
            helper.setSubject("[Devs] 이메일 인증 코드");
            helper.setText(
                    "이메일 인증 코드는 " + code + "입니다. 5분 안에 입력해 주세요.",
                    TEMPLATE.replace("{{code}}", code)
            );
            helper.addInline("devs-logo", LOGO, "image/png");
        };
    }

    private static String loadTemplate() {
        try {
            return StreamUtils.copyToString(
                    new ClassPathResource(TEMPLATE_PATH).getInputStream(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private String generateCode() {
        return String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));
    }

}
