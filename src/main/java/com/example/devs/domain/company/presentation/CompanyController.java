package com.example.devs.domain.company.presentation;

import com.example.devs.domain.company.presentation.dto.response.company_skill.CommonSkillListResponse;
import com.example.devs.domain.company.presentation.dto.response.CompanyListResponse;
import com.example.devs.domain.company.presentation.dto.response.company_match_rate.CompanyMatchRateListResponse;
import com.example.devs.domain.company.presentation.dto.response.company_posting.CompanyPostingCountListResponse;
import com.example.devs.domain.company.presentation.dto.response.company_skill_mention.SkillMentionListResponse;
import com.example.devs.domain.company.presentation.dto.response.company_top_skill.CompanyTopSkillListResponse;
import com.example.devs.domain.company.service.CommonSkillQueryService;
import com.example.devs.domain.company.service.CompanyAllQueryService;
import com.example.devs.domain.company.service.CompanyMatchRateQueryService;
import com.example.devs.domain.company.service.CompanyPostingCountQueryService;
import com.example.devs.domain.company.service.CompanyTopSkillQueryService;
import com.example.devs.domain.company.service.SkillMentionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyAllQueryService companyAllQueryService;
    private final CompanyPostingCountQueryService companyPostingCountQueryService;
    private final CompanyTopSkillQueryService companyTopSkillQueryService;
    private final CompanyMatchRateQueryService companyMatchRateQueryService;
    private final CommonSkillQueryService commonSkillQueryService;
    private final SkillMentionQueryService skillMentionQueryService;

    @GetMapping
    public CompanyListResponse getCompanies(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return companyAllQueryService.execute(keyword, pageable);
    }

    @GetMapping("/compare/posting-counts")
    public CompanyPostingCountListResponse getPostingCounts(
            @RequestParam(name = "company_ids") List<Long> companyIds
    ) {
        return companyPostingCountQueryService.execute(companyIds);
    }

    @GetMapping("/compare/top-skills")
    public CompanyTopSkillListResponse getTopSkills(
            @RequestParam(name = "company_ids") List<Long> companyIds
    ) {
        return companyTopSkillQueryService.execute(companyIds);
    }

    @GetMapping("/compare/match-rates")
    public CompanyMatchRateListResponse getMatchRates(
            @RequestParam(name = "company_ids") List<Long> companyIds
    ) {
        return companyMatchRateQueryService.execute(companyIds);
    }

    @GetMapping("/compare/common-skills")
    public CommonSkillListResponse getCommonSkills(
            @RequestParam(name = "company_ids") List<Long> companyIds
    ) {
        return commonSkillQueryService.execute(companyIds);
    }

    @GetMapping("/compare/skill-mentions")
    public SkillMentionListResponse getSkillMentions(
            @RequestParam(name = "company_ids") List<Long> companyIds
    ) {
        return skillMentionQueryService.execute(companyIds);
    }
}
