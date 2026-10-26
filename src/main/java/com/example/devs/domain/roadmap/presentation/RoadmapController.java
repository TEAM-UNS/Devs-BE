package com.example.devs.domain.roadmap.presentation;

import com.example.devs.domain.roadmap.presentation.dto.request.RoadmapCreateRequest;
import com.example.devs.domain.roadmap.presentation.dto.response.RoadmapCreateResponse;
import com.example.devs.domain.roadmap.presentation.dto.response.RoadmapStatusResponse;
import com.example.devs.domain.roadmap.service.RoadmapCreateService;
import com.example.devs.domain.roadmap.service.RoadmapStatusQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roadmap")
public class RoadmapController {
    private final RoadmapCreateService roadmapCreateService;
    private final RoadmapStatusQueryService roadmapStatusQueryService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RoadmapCreateResponse create(@Valid @RequestBody RoadmapCreateRequest request) {
        return roadmapCreateService.execute(request);
    }

    @GetMapping("/{roadmapId}/status")
    public RoadmapStatusResponse getStatus(@PathVariable Long roadmapId) {
        return roadmapStatusQueryService.execute(roadmapId);
    }
}
