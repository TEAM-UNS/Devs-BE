package com.example.devs.domain.company.domain.repository;

import com.example.devs.domain.company.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
