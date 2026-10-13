package com.example.devs.domain.company.service;

import com.example.devs.domain.company.domain.Company;
import com.example.devs.domain.company.domain.repository.CompanyQueryRepository;
import com.example.devs.domain.company.presentation.dto.response.CompanyPostingCountListResponse;
import com.example.devs.domain.company.presentation.dto.response.CompanyPostingCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyPostingCountQueryService {
    private final ComparedCompanyReader comparedCompanyReader;
    private final CompanyQueryRepository companyQueryRepository;

    @Transactional(readOnly = true)
    public CompanyPostingCountListResponse execute(List<Long> companyIds) {
        List<Company> companies = comparedCompanyReader.read(companyIds);
        List<Long> ids = companies.stream()
                .map(Company::getId)
                .toList();

        Map<Long, Long> postingCounts = companyQueryRepository.countPostings(ids).stream()
                .collect(Collectors.toMap(
                        CompanyQueryRepository.CompanyPostingCount::companyId,
                        CompanyQueryRepository.CompanyPostingCount::postingCount
                ));

        List<CompanyPostingCountResponse> responses = companies.stream()
                .map(company -> CompanyPostingCountResponse.of(
                        company,
                        postingCounts.getOrDefault(company.getId(), 0L)
                ))
                .toList();

        return CompanyPostingCountListResponse.from(responses);
    }
}