package com.example.devs.domain.retrospect_like.presentation.dto.response;

import lombok.Builder;

@Builder
public record RetrospectLikeResponse(
        Long retrospectId,
        long likeCount,
        boolean liked
) {
    public static RetrospectLikeResponse of(Long retrospectId, long likeCount, boolean liked) {
        return RetrospectLikeResponse.builder()
                .retrospectId(retrospectId)
                .likeCount(likeCount)
                .liked(liked)
                .build();
    }
}
