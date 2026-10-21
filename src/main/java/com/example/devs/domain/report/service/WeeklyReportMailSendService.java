package com.example.devs.domain.report.service;

import com.example.devs.domain.report.domain.Report;
import com.example.devs.domain.report.domain.WeeklyReportMail;
import com.example.devs.domain.report.domain.repository.ReportRepository;
import com.example.devs.domain.report.exception.ReportMailSendFailedException;
import com.example.devs.domain.report.exception.ReportNotFoundException;
import com.example.devs.domain.tech_field.domain.TechField;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.exception.MajorNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeeklyReportMailSendService {
    public static final String TOTAL_MAJOR_NAME = "전체";
    private static final List<String> INLINE_IMAGES = List.of("report-logo", "arrow-up", "arrow-down", "arrow-same");

    private final ReportRepository reportRepository;
    private final TechFieldRepository techFieldRepository;
    private final WeeklyReportMailRenderer weeklyReportMailRenderer;
    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String emailSender;

    public void execute(Integer majorId, LocalDate baseDate, String email) {
        LocalDate weekStartDate = baseDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        Report report = reportRepository.findFirstByMajor_IdAndWeekStartDateOrderByCreatedAtDescIdDesc(majorId, weekStartDate)
                .orElseThrow(ReportNotFoundException::new);
        String majorName = majorId == null ? TOTAL_MAJOR_NAME : techFieldRepository.findById(majorId)
                .map(TechField::getName)
                .orElseThrow(MajorNotFoundException::new);

        send(report, majorName, email);
    }

    public void send(Report report, String majorName, String email) {
        WeeklyReportMail mail = weeklyReportMailRenderer.render(report, majorName, email);

        try {
            mailSender.send(mimeMessage -> {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                helper.setFrom(emailSender);
                helper.setTo(email);
                helper.setSubject(mail.subject());
                helper.setText(mail.text(), mail.html());
                for (String image : INLINE_IMAGES) {
                    if (mail.html().contains("cid:" + image + "\"")) {
                        helper.addInline(
                                image,
                                image + ".png",
                                new ClassPathResource("templates/mail/" + image + ".png"),
                                "image/png"
                        );
                    }
                }
            });
        } catch (MailException exception) {
            log.error("주간 리포트 메일 발송 실패. major={}, weekStartDate={}", majorName, report.getWeekStartDate(), exception);
            throw new ReportMailSendFailedException();
        }
    }
}
