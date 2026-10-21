package com.example.devs.domain.company.service;

import com.example.devs.domain.company.domain.Company;
import com.example.devs.domain.company.domain.repository.CompanyQueryRepository;
import com.example.devs.domain.company.presentation.dto.response.company_skill.CommonSkillListResponse;
import com.example.devs.domain.company.presentation.dto.response.company_skill.CommonSkillResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommonSkillQueryService {
    private static final int MIN_COMMON_COMPANY_COUNT = 2;
    private static final int COMMON_SKILL_LIMIT = 3;

    private final ComparedCompanyReader comparedCompanyReader;
    private final CompanyQueryRepository companyQueryRepository;

    @Transactional(readOnly = true)
    public CommonSkillListResponse execute(List<Long> companyIds) {
        List<Company> companies = comparedCompanyReader.read(companyIds);
        List<Long> ids = companies.stream()
                .map(Company::getId)
                .toList();

        List<CommonSkillResponse> responses = companyQueryRepository.findSkillCounts(ids).stream()
                .collect(Collectors.groupingBy(CompanyQueryRepository.CompanySkillCount::skillId))
                .values().stream()
                .filter(rows -> rows.size() >= MIN_COMMON_COMPANY_COUNT)
                .map(rows -> new CommonSkill(
                        rows.get(0).skillName(),
                        rows.size(),
                        rows.stream().mapToLong(CompanyQueryRepository.CompanySkillCount::postingCount).sum()
                ))
                .sorted(Comparator.comparingInt(CommonSkill::companyCount).reversed()
                        .thenComparing(Comparator.comparingLong(CommonSkill::totalPostingCount).reversed())
                        .thenComparing(CommonSkill::skillName))
                .limit(COMMON_SKILL_LIMIT)
                .map(skill -> CommonSkillResponse.of(skill.skillName(), skill.companyCount(), companies.size()))
                .toList();

        return CommonSkillListResponse.from(responses);
    }

    private record CommonSkill(String skillName, int companyCount, long totalPostingCount) {
    }
}
