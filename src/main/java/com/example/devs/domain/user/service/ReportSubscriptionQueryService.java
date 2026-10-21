package com.example.devs.domain.user.service;

import com.example.devs.domain.user.domain.User;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.UserNotFoundException;
import com.example.devs.domain.user.presentation.dto.response.ReportSubscriptionQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportSubscriptionQueryService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public ReportSubscriptionQueryResponse execute(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return ReportSubscriptionQueryResponse.from(user);
    }
}
