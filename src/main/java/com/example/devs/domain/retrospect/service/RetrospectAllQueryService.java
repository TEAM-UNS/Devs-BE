package com.example.devs.domain.retrospect.service;

import com.example.devs.domain.retrospect.domain.Retrospect;
import com.example.devs.domain.retrospect.domain.repository.RetrospectRepository;
import com.example.devs.domain.retrospect.presentation.dto.response.RetrospectAllQueryResponse;
import com.example.devs.domain.retrospect_like.domain.RetrospectLikeCount;
import com.example.devs.domain.retrospect_like.domain.RetrospectLikeRepository;
import com.example.devs.domain.user.facade.UserFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RetrospectAllQueryService {
    private final RetrospectRepository retrospectRepository;
    private final RetrospectLikeRepository retrospectLikeRepository;
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public List<RetrospectAllQueryResponse> execute() {
        Long userId = userFacade.getCurrentUserId();
        List<Retrospect> retrospects = retrospectRepository.findAllByOrderByCreatedAtDesc();
        if (retrospects.isEmpty()) {
            return List.of();
        }

        List<Long> retrospectIds = retrospects.stream().map(Retrospect::getId).toList();
        Map<Long, Long> likeCountByRetrospectId = retrospectLikeRepository.countByRetrospectIds(retrospectIds)
                .stream()
                .collect(Collectors.toMap(RetrospectLikeCount::retrospectId, RetrospectLikeCount::likeCount));
        Set<Long> likedRetrospectIds = new HashSet<>(
                retrospectLikeRepository.findLikedRetrospectIds(userId, retrospectIds)
        );

        return retrospects.stream()
                .map(retrospect -> RetrospectAllQueryResponse.of(
                        retrospect,
                        likeCountByRetrospectId.getOrDefault(retrospect.getId(), 0L),
                        likedRetrospectIds.contains(retrospect.getId())
                ))
                .toList();
    }
}
