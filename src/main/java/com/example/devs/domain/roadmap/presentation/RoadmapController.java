package com.example.devs.domain.roadmap.presentation;

import com.example.devs.domain.roadmap.presentation.dto.request.RoadmapCreateRequest;
import com.example.devs.domain.roadmap.presentation.dto.request.RoadmapItemCheckRequest;
import com.example.devs.domain.roadmap.presentation.dto.request.RoadmapRetrospectRequest;
import com.example.devs.domain.roadmap.presentation.dto.response.RoadmapCreateResponse;
import com.example.devs.domain.roadmap.presentation.dto.response.RoadmapItemCheckResponse;
import com.example.devs.domain.roadmap.presentation.dto.response.RoadmapResponse;
import com.example.devs.domain.roadmap.presentation.dto.response.RoadmapStatusResponse;
import com.example.devs.domain.roadmap.service.RoadmapCreateService;
import com.example.devs.domain.roadmap.service.RoadmapDayCompleteService;
import com.example.devs.domain.roadmap.service.RoadmapItemCheckService;
import com.example.devs.domain.roadmap.service.RoadmapMyQueryService;
import com.example.devs.domain.roadmap.service.RoadmapStatusQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private final RoadmapMyQueryService roadmapMyQueryService;
    private final RoadmapDayCompleteService roadmapDayCompleteService;
    private final RoadmapItemCheckService roadmapItemCheckService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RoadmapCreateResponse create(@Valid @RequestBody RoadmapCreateRequest request) {
        return roadmapCreateService.execute(request);
    }

    @GetMapping
    public RoadmapResponse getMyRoadmap() {
        return roadmapMyQueryService.execute();
    }

    @GetMapping("/{roadmapId}/status")
    public RoadmapStatusResponse getStatus(@PathVariable Long roadmapId) {
        return roadmapStatusQueryService.execute(roadmapId);
    }

    @PatchMapping("/items/{itemId}")
    public RoadmapItemCheckResponse checkItem(
            @PathVariable Long itemId,
            @Valid @RequestBody RoadmapItemCheckRequest request
    ) {
        return roadmapItemCheckService.execute(itemId, request);
    }

    @PostMapping("/days/{dayNo}/complete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void completeDay(@PathVariable int dayNo, @Valid @RequestBody RoadmapRetrospectRequest request) {
        roadmapDayCompleteService.execute(dayNo, request);
    }
}
