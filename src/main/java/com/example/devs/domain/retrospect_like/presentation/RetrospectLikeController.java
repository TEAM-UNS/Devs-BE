package com.example.devs.domain.retrospect_like.presentation;

import com.example.devs.domain.retrospect_like.presentation.dto.response.RetrospectLikeResponse;
import com.example.devs.domain.retrospect_like.service.RetrospectLikeDeleteService;
import com.example.devs.domain.retrospect_like.service.RetrospectLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like/{retrospectId}")
public class RetrospectLikeController {
    private final RetrospectLikeService retrospectLikeService;
    private final RetrospectLikeDeleteService retrospectLikeDeleteService;

    @PostMapping
    public RetrospectLikeResponse like(@PathVariable Long retrospectId) {
        return retrospectLikeService.execute(retrospectId);
    }

    @DeleteMapping
    public RetrospectLikeResponse unlike(@PathVariable Long retrospectId) {
        return retrospectLikeDeleteService.execute(retrospectId);
    }
}
