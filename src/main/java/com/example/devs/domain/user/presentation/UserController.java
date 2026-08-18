package com.example.devs.domain.user.presentation;

import com.example.devs.domain.user.presentation.dto.request.*;
import com.example.devs.domain.user.presentation.dto.response.AccessTokenResponse;
import com.example.devs.domain.user.presentation.dto.response.TokenResponse;
import com.example.devs.domain.user.service.*;
import com.example.devs.global.security.jwt.JwtPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserSignupService userSignupService;
    private final UserLoginService userLoginService;
    private final TokenReissueService tokenReissueService;
    private final EmailSendService emailSendService;
    private final EmailVerificationService emailVerificationService;
    private final UserMajorUpdateService userMajorUpdateService;
    private final UserTechStackUpdateService userTechStackUpdateService;

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

    @PostMapping("/reissue")
    public AccessTokenResponse reissue(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return tokenReissueService.execute(refreshToken);
    }

    @PutMapping("/major")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMajor(
            @AuthenticationPrincipal JwtPrincipal principal,
            @Valid @RequestBody UserMajorUpdateRequest request
    ) {
        userMajorUpdateService.execute(principal.userId(), request);
    }

    @PutMapping("/tech-stack")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTechStack(
            @AuthenticationPrincipal JwtPrincipal principal,
            @Valid @RequestBody UserTechStackUpdateRequest request
    ) {
        userTechStackUpdateService.execute(principal.userId(), request);
    }
}
