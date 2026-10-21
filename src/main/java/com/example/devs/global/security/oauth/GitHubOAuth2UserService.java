package com.example.devs.global.security.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GitHubOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private static final String GITHUB_REGISTRATION_ID = "github";
    private static final String EMAIL_API_URI = "/user/emails";
    private static final String API_VERSION = "2026-03-10";
    private static final MediaType GITHUB_MEDIA_TYPE =
            MediaType.parseMediaType("application/vnd.github+json");

    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
    private final RestClient restClient;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User githubUser = delegate.loadUser(userRequest);

        if (!GITHUB_REGISTRATION_ID.equals(
                userRequest.getClientRegistration().getRegistrationId()
        )) {
            return githubUser;
        }

        String email = loadVerifiedPrimaryEmail(
                userRequest.getAccessToken().getTokenValue()
        );
        Map<String, Object> attributes = new HashMap<>(githubUser.getAttributes());
        attributes.put("email", email);

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        return new DefaultOAuth2User(
                githubUser.getAuthorities(),
                attributes,
                userNameAttributeName
        );
    }

    private String loadVerifiedPrimaryEmail(String accessToken) {
        try {
            GitHubEmail[] emails = restClient.get()
                    .uri(EMAIL_API_URI)
                    .headers(headers -> {
                        headers.setBearerAuth(accessToken);
                        headers.setAccept(List.of(GITHUB_MEDIA_TYPE));
                        headers.set("X-GitHub-Api-Version", API_VERSION);
                    })
                    .retrieve()
                    .body(GitHubEmail[].class);

            if (emails == null) {
                throw invalidGitHubEmail(null);
            }

            return Arrays.stream(emails)
                    .filter(email -> email.primary() && email.verified())
                    .map(GitHubEmail::email)
                    .filter(email -> email != null && !email.isBlank())
                    .findFirst()
                    .orElseThrow(() -> invalidGitHubEmail(null));
        } catch (RestClientException exception) {
            throw invalidGitHubEmail(exception);
        }
    }

    private OAuth2AuthenticationException invalidGitHubEmail(Throwable cause) {
        OAuth2Error error = new OAuth2Error(
                "invalid_github_email",
                "GitHub의 인증된 기본 이메일을 확인할 수 없습니다.",
                null
        );
        return new OAuth2AuthenticationException(error, error.getDescription(), cause);
    }

    private record GitHubEmail(String email, boolean primary, boolean verified) {
    }
}
