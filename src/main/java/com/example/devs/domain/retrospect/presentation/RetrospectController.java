package com.example.devs.domain.retrospect.presentation;

import com.example.devs.domain.retrospect.presentation.dto.response.RetrospectAllQueryResponse;
import com.example.devs.domain.retrospect.service.RetrospectAllQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/retrospect")
public class RetrospectController {
    private final RetrospectAllQueryService retrospectAllQueryService;

    @GetMapping
    public List<RetrospectAllQueryResponse> getAll() {
        return retrospectAllQueryService.execute();
    }
}
