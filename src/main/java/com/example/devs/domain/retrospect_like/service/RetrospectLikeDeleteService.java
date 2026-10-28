package com.example.devs.domain.retrospect_like.service;

import com.example.devs.domain.retrospect_like.domain.RetrospectLike;
import com.example.devs.domain.retrospect_like.domain.RetrospectLikeRepository;
import com.example.devs.domain.retrospect_like.exception.RetrospectLikeNotFoundException;
import com.example.devs.domain.retrospect_like.presentation.dto.response.RetrospectLikeResponse;
import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RetrospectLikeDeleteService {

    private final RetrospectLikeRepository retrospectLikeRepository;
    private final UserFacade userFacade;

    @Transactional
    public RetrospectLikeResponse execute(Long retrospectId) {
        Long userId = userFacade.getCurrentUserId();
        RetrospectLike retrospectLike = retrospectLikeRepository.findByRetrospectIdAndUserId(retrospectId, userId)
                .orElseThrow(RetrospectLikeNotFoundException::new);

        retrospectLikeRepository.delete(retrospectLike);

        return RetrospectLikeResponse.of(retrospectId, retrospectLikeRepository.countByRetrospectId(retrospectId), false);
    }
}
