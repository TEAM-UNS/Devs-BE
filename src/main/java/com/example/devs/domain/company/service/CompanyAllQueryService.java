package com.example.devs.domain.company.service;

import com.example.devs.domain.company.domain.Company;
import com.example.devs.domain.company.domain.repository.CompanyRepository;
import com.example.devs.domain.company.presentation.dto.response.CompanyListResponse;
import com.example.devs.domain.company.presentation.dto.response.CompanyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class CompanyAllQueryService {

    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public CompanyListResponse execute(String keyword, Pageable pageable) {
        Page<Company> companies = StringUtils.hasText(keyword)
                ? companyRepository.findByNameContainingIgnoreCase(keyword.trim(), pageable)
                : companyRepository.findAll(pageable);

        return CompanyListResponse.from(companies.map(CompanyResponse::from));
    }
}
