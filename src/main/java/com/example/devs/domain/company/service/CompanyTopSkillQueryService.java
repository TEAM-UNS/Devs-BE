package com.example.devs.domain.company.service;

import com.example.devs.domain.company.domain.Company;
import com.example.devs.domain.company.domain.repository.CompanyQueryRepository;
import com.example.devs.domain.company.presentation.dto.response.CompanyTopSkillListResponse;
import com.example.devs.domain.company.presentation.dto.response.CompanyTopSkillResponse;
import com.example.devs.domain.company.presentation.dto.response.SkillRankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CompanyTopSkillQueryService {
    private static final int TOP_SKILL_LIMIT = 3;

    private final ComparedCompanyReader comparedCompanyReader;
    private final CompanyQueryRepository companyQueryRepository;

    @Transactional(readOnly = true)
    public CompanyTopSkillListResponse execute(List<Long> companyIds) {
        List<Company> companies = comparedCompanyReader.read(companyIds);
        List<Long> ids = companies.stream()
                .map(Company::getId)
                .toList();

        Map<Long, Long> postingCounts = companyQueryRepository.countPostings(ids).stream()
                .collect(Collectors.toMap(
                        CompanyQueryRepository.CompanyPostingCount::companyId,
                        CompanyQueryRepository.CompanyPostingCount::postingCount
                ));

        Map<Long, List<CompanyQueryRepository.CompanySkillCount>> skillsByCompany = companyQueryRepository.findSkillCounts(ids).stream()
                .collect(Collectors.groupingBy(CompanyQueryRepository.CompanySkillCount::companyId));

        List<CompanyTopSkillResponse> responses = companies.stream()
                .map(company -> CompanyTopSkillResponse.of(
                        company,
                        toSkillRanks(
                                skillsByCompany.getOrDefault(company.getId(), List.of()),
                                postingCounts.getOrDefault(company.getId(), 0L)
                        )
                ))
                .toList();

        return CompanyTopSkillListResponse.from(responses);
    }

    private List<SkillRankResponse> toSkillRanks(List<CompanyQueryRepository.CompanySkillCount> skills, long postingCount) {
        List<CompanyQueryRepository.CompanySkillCount> topSkills = skills.stream()
                .limit(TOP_SKILL_LIMIT)
                .toList();

        return IntStream.range(0, topSkills.size())
                .mapToObj(index -> SkillRankResponse.of(
                        index + 1,
                        topSkills.get(index).skillName(),
                        (int) Math.round(topSkills.get(index).postingCount() * 100.0 / postingCount)
                ))
                .toList();
    }
}