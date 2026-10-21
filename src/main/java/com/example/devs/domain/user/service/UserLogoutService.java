package com.example.devs.domain.user.service;

import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogoutService {
    private final UserFacade userFacade;
    private final RefreshTokenService refreshTokenService;

    public void execute() {
        refreshTokenService.delete(userFacade.getCurrentUserId());
    }
}
