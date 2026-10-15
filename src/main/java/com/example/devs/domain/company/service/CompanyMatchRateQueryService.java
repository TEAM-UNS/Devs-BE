package com.example.devs.domain.company.service;

import com.example.devs.domain.company.domain.Company;
import com.example.devs.domain.company.domain.repository.CompanyQueryRepository;
import com.example.devs.domain.company.presentation.dto.response.company_match_rate.CompanyMatchRateListResponse;
import com.example.devs.domain.company.presentation.dto.response.company_match_rate.CompanyMatchRateResponse;
import com.example.devs.domain.skill.domain.Skill;
import com.example.devs.domain.user.facade.UserFacade;
import com.example.devs.domain.user_skill.domain.repository.UserSkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyMatchRateQueryService {
    private static final int MATCH_SKILL_LIMIT = 10;

    private final ComparedCompanyReader comparedCompanyReader;
    private final CompanyQueryRepository companyQueryRepository;
    private final UserSkillRepository userSkillRepository;
    private final UserFacade userFacade;

    @Transactional(readOnly = true)
    public CompanyMatchRateListResponse execute(List<Long> companyIds) {
        List<Company> companies = comparedCompanyReader.read(companyIds);
        List<Long> ids = companies.stream()
                .map(Company::getId)
                .toList();

        Set<Integer> mySkillIds = userSkillRepository.findSkillsByUserId(userFacade.getCurrentUserId()).stream()
                .map(Skill::getId)
                .collect(Collectors.toSet());

        Map<Long, List<CompanyQueryRepository.CompanySkillCount>> skillsByCompany = companyQueryRepository.findSkillCounts(ids).stream()
                .collect(Collectors.groupingBy(CompanyQueryRepository.CompanySkillCount::companyId));

        List<CompanyMatchRateResponse> responses = companies.stream()
                .map(company -> CompanyMatchRateResponse.of(
                        company,
                        calculateMatchRate(skillsByCompany.getOrDefault(company.getId(), List.of()), mySkillIds)
                ))
                .toList();

        return CompanyMatchRateListResponse.from(responses);
    }

    private int calculateMatchRate(List<CompanyQueryRepository.CompanySkillCount> skills, Set<Integer> mySkillIds) {
        List<CompanyQueryRepository.CompanySkillCount> topSkills = skills.stream()
                .limit(MATCH_SKILL_LIMIT)
                .toList();
        if (topSkills.isEmpty()) {
            return 0;
        }

        long matchedCount = topSkills.stream()
                .filter(skill -> mySkillIds.contains(skill.skillId()))
                .count();
        return (int) Math.round(matchedCount * 100.0 / topSkills.size());
    }
}
