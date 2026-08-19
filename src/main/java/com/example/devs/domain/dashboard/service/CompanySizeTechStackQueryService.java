package com.example.devs.domain.dashboard.service;

import com.example.devs.domain.company.domain.CompanySize;
import com.example.devs.domain.dashboard.domain.repository.DashboardQueryRepository;
import com.example.devs.domain.dashboard.presentation.dto.response.CompanySizeTechStackListResponse;
import com.example.devs.domain.dashboard.presentation.dto.response.CompanySizeTechStackResponse;
import com.example.devs.domain.tech_field.domain.TechField;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.exception.MajorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CompanySizeTechStackQueryService {
    private static final int TECH_STACK_LIMIT = 9;

    private final DashboardQueryRepository dashboardQueryRepository;
    private final TechFieldRepository techFieldRepository;

    @Transactional(readOnly = true)
    public CompanySizeTechStackListResponse execute(
            CompanySize companySize,
            Integer majorId
    ) {
        TechField major = majorId == null
                ? null
                : techFieldRepository.findById(majorId)
                        .orElseThrow(MajorNotFoundException::new);

        List<DashboardQueryRepository.CompanySizeTechStack> techStacks = major == null
                ? dashboardQueryRepository.findTechStacksByCompanySize(
                        companySize.databaseValues(),
                        TECH_STACK_LIMIT
                )
                : dashboardQueryRepository.findTechStacksByCompanySizeAndMajor(
                        companySize.databaseValues(),
                        major.getId(),
                        TECH_STACK_LIMIT
                );

        List<CompanySizeTechStackResponse> responses = IntStream.range(0, techStacks.size())
                .mapToObj(index -> new CompanySizeTechStackResponse(
                        index + 1,
                        techStacks.get(index).name(),
                        techStacks.get(index).percentage()
                ))
                .toList();

        String category = major == null
                ? null
                : major.getCode().toUpperCase(Locale.ROOT);

        return new CompanySizeTechStackListResponse(
                companySize.name(),
                category,
                responses
        );
    }
}
