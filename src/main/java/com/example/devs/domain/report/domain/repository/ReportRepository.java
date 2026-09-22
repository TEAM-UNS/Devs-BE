package com.example.devs.domain.report.domain.repository;

import com.example.devs.domain.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findFirstByMajor_IdAndWeekStartDateOrderByCreatedAtDescIdDesc(
            Integer majorId,
            LocalDate weekStartDate
    );
}
