package com.example.devs.domain.company.service;

import com.example.devs.domain.company.domain.Company;
import com.example.devs.domain.company.domain.repository.CompanyQueryRepository;
import com.example.devs.domain.company.presentation.dto.response.company_skill_mention.CompanyMentionCountResponse;
import com.example.devs.domain.company.presentation.dto.response.company_skill_mention.SkillMentionListResponse;
import com.example.devs.domain.company.presentation.dto.response.company_skill_mention.SkillMentionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillMentionQueryService {
    private static final int MENTION_SKILL_LIMIT = 3;

    private final ComparedCompanyReader comparedCompanyReader;
    private final CompanyQueryRepository companyQueryRepository;

    @Transactional(readOnly = true)
    public SkillMentionListResponse execute(List<Long> companyIds) {
        List<Company> companies = comparedCompanyReader.read(companyIds);
        List<Long> ids = companies.stream()
                .map(Company::getId)
                .toList();

        List<SkillMentionResponse> responses = companyQueryRepository.findSkillCounts(ids).stream()
                .collect(Collectors.groupingBy(CompanyQueryRepository.CompanySkillCount::skillId))
                .values().stream()
                .map(rows -> new MentionedSkill(
                        rows.get(0).skillName(),
                        rows.stream().mapToLong(CompanyQueryRepository.CompanySkillCount::postingCount).sum(),
                        rows
                ))
                .sorted(Comparator.comparingLong(MentionedSkill::totalPostingCount).reversed()
                        .thenComparing(MentionedSkill::skillName))
                .limit(MENTION_SKILL_LIMIT)
                .map(skill -> SkillMentionResponse.of(skill.skillName(), toCompanyCounts(skill.rows(), ids)))
                .toList();

        return SkillMentionListResponse.from(responses);
    }

    private List<CompanyMentionCountResponse> toCompanyCounts(
            List<CompanyQueryRepository.CompanySkillCount> rows,
            List<Long> ids
    ) {
        Map<Long, Long> countsByCompany = rows.stream()
                .collect(Collectors.toMap(
                        CompanyQueryRepository.CompanySkillCount::companyId,
                        CompanyQueryRepository.CompanySkillCount::postingCount
                ));

        return ids.stream()
                .map(id -> CompanyMentionCountResponse.of(id, countsByCompany.getOrDefault(id, 0L)))
                .toList();
    }

    private record MentionedSkill(
            String skillName,
            long totalPostingCount,
            List<CompanyQueryRepository.CompanySkillCount> rows
    ) {
    }
}