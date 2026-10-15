package com.example.devs.domain.company.service;

import com.example.devs.domain.company.domain.Company;
import com.example.devs.domain.company.domain.repository.CompanyRepository;
import com.example.devs.domain.company.exception.CompanyNotFoundException;
import com.example.devs.domain.company.exception.InvalidCompareCompanyCountException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ComparedCompanyReader {
    private static final int MIN_COMPANY_COUNT = 2;
    private static final int MAX_COMPANY_COUNT = 5;

    private final CompanyRepository companyRepository;

    public List<Company> read(List<Long> companyIds) {
        List<Long> distinctIds = companyIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (distinctIds.size() < MIN_COMPANY_COUNT || distinctIds.size() > MAX_COMPANY_COUNT) {
            throw new InvalidCompareCompanyCountException();
        }

        Map<Long, Company> companiesById = companyRepository.findAllById(distinctIds).stream()
                .collect(Collectors.toMap(Company::getId, Function.identity()));
        if (companiesById.size() != distinctIds.size()) {
            throw new CompanyNotFoundException();
        }

        return distinctIds.stream()
                .map(companiesById::get)
                .toList();
    }
}
