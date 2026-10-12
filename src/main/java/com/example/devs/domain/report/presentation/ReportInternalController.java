package com.example.devs.domain.report.presentation;

import com.example.devs.domain.report.presentation.dto.request.LlmReportReadyRequest;
import com.example.devs.domain.report.service.HandleLlmReportReadyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** AI 서버가 도커 내부 네트워크로 호출하는 API. */
@RestController
@RequestMapping("/internal/reports")
@RequiredArgsConstructor
public class ReportInternalController {
    private final HandleLlmReportReadyService handleLlmReportReadyService;

    @PostMapping("/ready")
    public void ready(@Valid @RequestBody LlmReportReadyRequest request) {
        handleLlmReportReadyService.execute(request);
    }
}
