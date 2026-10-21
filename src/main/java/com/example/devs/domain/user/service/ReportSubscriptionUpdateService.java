package com.example.devs.domain.user.service;

import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.exception.MajorNotFoundException;
import com.example.devs.domain.user.domain.User;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.UserNotFoundException;
import com.example.devs.domain.user.presentation.dto.request.ReportSubscriptionUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportSubscriptionUpdateService {
    private final UserRepository userRepository;
    private final TechFieldRepository techFieldRepository;

    @Transactional
    public void execute(Long userId, ReportSubscriptionUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        if (techFieldRepository.findAllById(request.majorIds()).size() != request.majorIds().size()) {
            throw new MajorNotFoundException();
        }

        user.updateReportSubscription(request.totalReport(), request.majorReport(), request.majorIds());
    }
}
