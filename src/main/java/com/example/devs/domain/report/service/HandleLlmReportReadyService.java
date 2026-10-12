package com.example.devs.domain.report.service;

import com.example.devs.domain.report.presentation.dto.request.LlmReportReadyRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * AI 서버의 LLM 리포트 생성 완료 통지를 처리한다.
 * 같은 주차 통지가 여러 번 올 수 있고, 10초 안에 응답하지 않으면 AI 서버가 재전송하므로 무거운 작업은 하지 않는다.
 */
@Slf4j
@Service
public class HandleLlmReportReadyService {

    public void execute(LlmReportReadyRequest request) {
        if (request.pending() > 0) {
            log.info("주간 LLM 리포트 생성 진행 중. weekStartDate={}, generated={}, skipped={}, pending={}",
                    request.weekStartDate(), request.generated(), request.skipped(), request.pending());
            return;
        }
        log.info("주간 LLM 리포트 생성 완료. weekStartDate={}, total={}",
                request.weekStartDate(), request.total());
    }
}
