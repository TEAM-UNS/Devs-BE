package com.example.devs.domain.user.service;

import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.presentation.dto.response.UserMyQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserMyQueryService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserMyQueryResponse execute()
}
