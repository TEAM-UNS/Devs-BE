package com.example.devs.domain.retrospect.service;

import com.example.devs.domain.retrospect.domain.repository.RetrospectRepository;
import com.example.devs.domain.retrospect.presentation.dto.response.RetrospectAllQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RetrospectAllQueryService {
    private final RetrospectRepository retrospectRepository;

    @Transactional(readOnly = true)
    public List<RetrospectAllQueryResponse> execute() {
        return retrospectRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(RetrospectAllQueryResponse::from)
                .toList();
    }
}
