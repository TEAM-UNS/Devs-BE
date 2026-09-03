package com.example.devs.domain.company.service;

import com.example.devs.domain.company.domain.Company;
import com.example.devs.domain.company.domain.repository.CompanyRepository;
import com.example.devs.domain.company.presentation.dto.response.CompanyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyAllQueryService {

    private final CompanyRepository companyRepository;

    @Transactional(readOnly = true)
    public List<CompanyResponse> execute(){
        return companyRepository.findAll().stream()
                .map(CompanyResponse::from)
                .toList();
    }
}
