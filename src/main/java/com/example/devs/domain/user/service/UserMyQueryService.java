package com.example.devs.domain.user.service;

import com.example.devs.domain.user.domain.User;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.exception.UserNotFoundException;
import com.example.devs.domain.user.facade.UserFacade;
import com.example.devs.domain.user.presentation.dto.response.UserMyQueryResponse;
import com.example.devs.domain.user_major.domain.repository.UserMajorRepository;
import com.example.devs.domain.user_skill.domain.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserMyQueryService {

    private final UserRepository userRepository;
    private final UserMajorRepository userMajorRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public UserMyQueryResponse execute() {
        Long userId = userFacade.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        return UserMyQueryResponse.from(
                user,
                userMajorRepository.findMajorsByUserId(userId),
                userSkillRepository.findSkillsByUserId(userId)
        );
    }
}
