package com.example.devs.domain.company.presentation.dto.response.company_skill_mention;

public record CompanyMentionCountResponse(Long companyId, long postingCount) {
    public static CompanyMentionCountResponse of(Long companyId, long postingCount) {
        return new CompanyMentionCountResponse(companyId, postingCount);
    }
}
