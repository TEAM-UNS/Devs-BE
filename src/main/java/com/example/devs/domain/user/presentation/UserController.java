package com.example.devs.domain.user.presentation;

import com.example.devs.domain.user.presentation.dto.request.EmailVerificationConfirmRequest;
import com.example.devs.domain.user.presentation.dto.request.EmailVerificationSendRequest;
import com.example.devs.domain.user.presentation.dto.request.UserLoginRequest;
import com.example.devs.domain.user.presentation.dto.request.UserSignupRequest;
import com.example.devs.domain.user.presentation.dto.response.TokenResponse;
import com.example.devs.domain.user.service.EmailSendService;
import com.example.devs.domain.user.service.EmailVerificationService;
import com.example.devs.domain.user.service.UserLoginService;
import com.example.devs.domain.user.service.UserSignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserSignupService userSignupService;
    private final UserLoginService userLoginService;
    private final EmailSendService emailSendService;
    private final EmailVerificationService emailVerificationService;

    @PostMapping("/email/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendVerificationCode(@Valid @RequestBody EmailVerificationSendRequest request) {
        emailSendService.execute(request);
    }

    @PostMapping("/email/verify")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void confirmVerificationCode(@Valid @RequestBody EmailVerificationConfirmRequest request) {
        emailVerificationService.execute(request);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public void signup(@Valid @RequestBody UserSignupRequest request) {
        userSignupService.execute(request);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody UserLoginRequest request) {
        return userLoginService.execute(request);
    }
}
