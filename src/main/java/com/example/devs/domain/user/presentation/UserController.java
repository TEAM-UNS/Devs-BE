package com.example.devs.domain.user.presentation;

import com.example.devs.domain.user.presentation.dto.request.*;
import com.example.devs.domain.user.presentation.dto.response.AccessTokenResponse;
import com.example.devs.domain.user.presentation.dto.response.OAuthTokenResponse;
import com.example.devs.domain.user.presentation.dto.response.TokenResponse;
import com.example.devs.domain.user.service.*;
import com.example.devs.global.security.jwt.JwtPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
    private final GoogleOAuthLoginService googleOAuthLoginService;
    private final GitHubOAuthLoginService gitHubOAuthLoginService;

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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public TokenResponse login(@Valid @RequestBody UserLoginRequest request) {
        return userLoginService.execute(request);
    }

    @PostMapping("/reissue")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public AccessTokenResponse reissue(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return tokenReissueService.execute(refreshToken);
    }

    @PostMapping("/oauth/google/token")
    public OAuthTokenResponse issueGoogleOAuthToken(
            @AuthenticationPrincipal OidcUser oidcUser,
            HttpServletRequest servletRequest
    ) {
        OAuthTokenResponse response = googleOAuthLoginService.execute(oidcUser);
        invalidateSession(servletRequest);
        return response;
    }

    @PostMapping("/oauth/github/token")
    public OAuthTokenResponse issueGitHubOAuthToken(
            @AuthenticationPrincipal OAuth2User oauth2User,
            HttpServletRequest servletRequest
    ) {
        OAuthTokenResponse response = gitHubOAuthLoginService.execute(oauth2User);
        invalidateSession(servletRequest);
        return response;
    }

    private void invalidateSession(HttpServletRequest servletRequest) {
        if (servletRequest.getSession(false) != null) {
            servletRequest.getSession(false).invalidate();
        }
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
