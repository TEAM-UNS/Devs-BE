package com.example.devs.domain.user.exception;

import com.example.devs.global.error.exception.DevsException;
import com.example.devs.global.error.exception.ErrorCode;

public class InvalidGitHubOAuthProfileException extends DevsException {
    public InvalidGitHubOAuthProfileException() {
        super(ErrorCode.INVALID_GITHUB_OAUTH_PROFILE);
    }
}
