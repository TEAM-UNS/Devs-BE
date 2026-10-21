package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.Report;
import com.example.devs.domain.report.domain.repository.ReportRepository;
import com.example.devs.domain.report.exception.ReportMailSendFailedException;
import com.example.devs.domain.tech_field.domain.TechField;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.user.domain.repository.MajorReportSubscriberProjection;
import com.example.devs.domain.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendWeeklyReportMailsService {
    private final UserRepository userRepository;
    private final TechFieldRepository techFieldRepository;
    private final ReportRepository reportRepository;
    private final WeeklyReportMailSendService weeklyReportMailSendService;

    public void execute(LocalDate baseDate) {
        LocalDate weekStartDate = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        int sent = 0;
        int failed = 0;

        for (MailTarget target : findTargets()) {
            if (target.emails().isEmpty()) {
                continue;
            }

            Optional<Report> report = reportRepository
                    .findFirstByMajor_IdAndWeekStartDateOrderByCreatedAtDescIdDesc(target.majorId(), weekStartDate);
            if (report.isEmpty()) {
                log.warn("주간 리포트가 없어 메일을 보내지 않음. major={}, weekStartDate={}", target.majorName(), weekStartDate);
                continue;
            }

            for (String email : target.emails()) {
                try {
                    weeklyReportMailSendService.send(report.get(), target.majorName(), email);
                    sent++;
                } catch (ReportMailSendFailedException exception) {
                    failed++;
                }
            }
        }

        log.info("주간 리포트 메일 발송 완료. weekStartDate={}, sent={}, failed={}", weekStartDate, sent, failed);
    }

    private List<MailTarget> findTargets() {
        Map<Integer, List<String>> emailsByMajor = userRepository.findMajorReportSubscribers().stream()
                .collect(Collectors.groupingBy(
                        MajorReportSubscriberProjection::majorId,
                        Collectors.mapping(MajorReportSubscriberProjection::email, Collectors.toList())
                ));

        List<MailTarget> targets = new ArrayList<>();
        targets.add(new MailTarget(
                null,
                WeeklyReportMailSendService.TOTAL_MAJOR_NAME,
                userRepository.findTotalReportSubscriberEmails()
        ));
        for (TechField major : techFieldRepository.findAllByOrderBySortOrderAsc()) {
            targets.add(new MailTarget(
                    major.getId(),
                    major.getName(),
                    emailsByMajor.getOrDefault(major.getId(), List.of())
            ));
        }
        return targets;
    }

    private record MailTarget(Integer majorId, String majorName, List<String> emails) {
    }
}
