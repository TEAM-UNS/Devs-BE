package com.example.devs.domain.user.facade;

import com.example.devs.domain.user.exception.UnauthenticatedUserException;
import com.example.devs.global.security.jwt.JwtPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof JwtPrincipal principal)) {
            throw new UnauthenticatedUserException();
        }
        return principal.userId();
    }
}
