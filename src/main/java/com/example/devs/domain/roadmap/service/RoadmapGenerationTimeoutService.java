package com.example.devs.domain.roadmap.service;

import com.example.devs.domain.roadmap.domain.RoadmapStatus;
import com.example.devs.domain.roadmap.domain.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoadmapGenerationTimeoutService {
    private static final Duration TIMEOUT = Duration.ofMinutes(15);
    private static final List<RoadmapStatus> IN_PROGRESS_STATUSES =
            List.of(RoadmapStatus.PENDING, RoadmapStatus.GENERATING);
    private static final String TIMEOUT_MESSAGE = "로드맵 생성 시간이 초과되었습니다. 다시 생성해 주세요.";

    private final RoadmapRepository roadmapRepository;
    private final Clock clock;

    @Transactional
    public void execute() {
        int failedCount = roadmapRepository.failInProgressCreatedBefore(
                IN_PROGRESS_STATUSES,
                OffsetDateTime.now(clock).minus(TIMEOUT),
                RoadmapStatus.FAILED,
                TIMEOUT_MESSAGE
        );

        if (failedCount > 0) {
            log.warn("생성 시간이 초과된 로드맵을 실패 처리했습니다. count={}", failedCount);
        }
    }
}
