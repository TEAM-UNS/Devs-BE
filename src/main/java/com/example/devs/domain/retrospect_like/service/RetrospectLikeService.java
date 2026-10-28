package com.example.devs.domain.retrospect_like.service;

import com.example.devs.domain.retrospect.domain.Retrospect;
import com.example.devs.domain.retrospect.domain.repository.RetrospectRepository;
import com.example.devs.domain.retrospect.exception.RetrospectNotFoundException;
import com.example.devs.domain.retrospect_like.domain.RetrospectLike;
import com.example.devs.domain.retrospect_like.domain.RetrospectLikeRepository;
import com.example.devs.domain.retrospect_like.exception.RetrospectAlreadyLikedException;
import com.example.devs.domain.retrospect_like.presentation.dto.response.RetrospectLikeResponse;
import com.example.devs.domain.user.domain.repository.UserRepository;
import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RetrospectLikeService {

    private final RetrospectLikeRepository retrospectLikeRepository;
    private final RetrospectRepository retrospectRepository;
    private final UserRepository userRepository;
    private final UserFacade userFacade;

    @Transactional
    public RetrospectLikeResponse execute(Long retrospectId) {
        Long userId = userFacade.getCurrentUserId();
        Retrospect retrospect = retrospectRepository.findById(retrospectId)
                .orElseThrow(RetrospectNotFoundException::new);

        if (retrospectLikeRepository.existsByRetrospectIdAndUserId(retrospectId, userId)) {
            throw new RetrospectAlreadyLikedException();
        }

        try {
            retrospectLikeRepository.save(RetrospectLike.builder()
                    .retrospect(retrospect)
                    .user(userRepository.getReferenceById(userId))
                    .build());
        } catch (DataIntegrityViolationException e) {
            throw new RetrospectAlreadyLikedException();
        }

        return RetrospectLikeResponse.of(retrospectId, retrospectLikeRepository.countByRetrospectId(retrospectId), true);
    }
}
