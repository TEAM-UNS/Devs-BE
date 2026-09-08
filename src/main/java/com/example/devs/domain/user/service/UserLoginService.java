package com.example.devs.domain.user.service;

import com.example.devs.domain.user.domain.User;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.InvalidLoginCredentialsException;
import com.example.devs.domain.user.presentation.dto.request.UserLoginRequest;
import com.example.devs.domain.user.presentation.dto.response.LoginResponse;
import com.example.devs.domain.user.presentation.dto.response.UserMajorResponse;
import com.example.devs.domain.user_major.domain.repository.UserMajorRepository;
import com.example.devs.domain.user.util.EmailNormalizer;
import com.example.devs.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final UserRepository userRepository;
    private final UserMajorRepository userMajorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public LoginResponse execute(UserLoginRequest request) {
        String email = EmailNormalizer.normalize(request.email());
        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidLoginCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidLoginCredentialsException();
        }

        List<UserMajorResponse> majors = userMajorRepository.findMajorsByUserId(user.getId())
                .stream()
                .map(UserMajorResponse::from)
                .toList();

        String accessToken = jwtProvider.generateAccessToken(user);
        String refreshToken = jwtProvider.generateRefreshToken(user);

        refreshTokenService.save(
                user.getId(),
                refreshToken
        );

        return new LoginResponse(
                accessToken,
                refreshToken,
                majors
        );
    }
}
