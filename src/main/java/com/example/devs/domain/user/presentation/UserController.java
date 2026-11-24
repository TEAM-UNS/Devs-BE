package com.example.devs.domain.user.presentation;

import com.example.devs.domain.user.presentation.dto.request.EmailVerificationConfirmRequest;
import com.example.devs.domain.user.presentation.dto.request.EmailVerificationSendRequest;
import com.example.devs.domain.user.presentation.dto.request.ReportSubscriptionUpdateRequest;
import com.example.devs.domain.user.presentation.dto.request.UserLoginRequest;
import com.example.devs.domain.user.presentation.dto.request.UserMajorUpdateRequest;
import com.example.devs.domain.user.presentation.dto.request.UserSignupRequest;
import com.example.devs.domain.user.presentation.dto.request.UserTechStackUpdateRequest;
import com.example.devs.domain.user.presentation.dto.response.AccessTokenResponse;
import com.example.devs.domain.user.presentation.dto.response.OAuthLoginResponse;
import com.example.devs.domain.user.presentation.dto.response.LoginResponse;
import com.example.devs.domain.user.presentation.dto.response.ReportSubscriptionQueryResponse;
import com.example.devs.domain.user.presentation.dto.response.UserMyQueryResponse;
import com.example.devs.domain.user.service.EmailSendService;
import com.example.devs.domain.user.service.EmailVerificationService;
import com.example.devs.domain.user.service.GitHubOAuthLoginService;
import com.example.devs.domain.user.service.GoogleOAuthLoginService;
import com.example.devs.domain.user.service.ReportSubscriptionQueryService;
import com.example.devs.domain.user.service.ReportSubscriptionUpdateService;
import com.example.devs.domain.user.service.TokenReissueService;
import com.example.devs.domain.user.service.UserLoginService;
import com.example.devs.domain.user.service.UserLogoutService;
import com.example.devs.domain.user.service.UserMajorUpdateService;
import com.example.devs.domain.user.service.UserMyQueryService;
import com.example.devs.domain.user.service.UserSignupService;
import com.example.devs.domain.user.service.UserTechStackUpdateService;
import com.example.devs.global.security.jwt.JwtPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    private final UserMyQueryService userMyQueryService;
    private final UserLogoutService userLogoutService;
    private final ReportSubscriptionUpdateService reportSubscriptionUpdateService;
    private final ReportSubscriptionQueryService reportSubscriptionQueryService;

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
    public LoginResponse login(@Valid @RequestBody UserLoginRequest request) {
        return userLoginService.execute(request);
    }

    @PostMapping("/reissue")
    public AccessTokenResponse reissue(@RequestHeader("X-Refresh-Token") String refreshToken) {
        return tokenReissueService.execute(refreshToken);
    }

    @PostMapping("/oauth/google/token")
    public OAuthLoginResponse issueGoogleOAuthToken(
            @AuthenticationPrincipal OidcUser oidcUser,
            HttpServletRequest servletRequest
    ) {
        OAuthLoginResponse response = googleOAuthLoginService.execute(oidcUser);
        invalidateSession(servletRequest);
        return response;
    }

    @PostMapping("/oauth/github/token")
    public OAuthLoginResponse issueGitHubOAuthToken(
            @AuthenticationPrincipal OAuth2User oauth2User,
            HttpServletRequest servletRequest
    ) {
        OAuthLoginResponse response = gitHubOAuthLoginService.execute(oauth2User);
        invalidateSession(servletRequest);
        return response;
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
        userLogoutService.execute();
    }

    @GetMapping("/my")
    public UserMyQueryResponse getMy() {
        return userMyQueryService.execute();
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
    private void invalidateSession(HttpServletRequest servletRequest) {
        if (servletRequest.getSession(false) != null) {
            servletRequest.getSession(false).invalidate();
        }
    }

    @GetMapping("/report-subscription")
    public ReportSubscriptionQueryResponse getReportSubscription(
            @AuthenticationPrincipal JwtPrincipal principal
    ) {
        return reportSubscriptionQueryService.execute(principal.userId());
    }

    @PutMapping("/report-subscription")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateReportSubscription(
            @AuthenticationPrincipal JwtPrincipal principal,
            @Valid @RequestBody ReportSubscriptionUpdateRequest request
    ) {
        reportSubscriptionUpdateService.execute(principal.userId(), request);
    }
}
