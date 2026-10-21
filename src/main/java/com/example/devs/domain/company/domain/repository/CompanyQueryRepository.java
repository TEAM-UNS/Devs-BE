package com.example.devs.domain.company.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CompanyQueryRepository {
    private final JdbcClient jdbcClient;

    public List<CompanyPostingCount> countPostings(Collection<Long> companyIds) {
        return jdbcClient.sql("""
                        select company_id, count(*) as posting_count
                        from market.job_posting
                        where company_id in (:companyIds)
                        group by company_id
                        """)
                .param("companyIds", companyIds)
                .query((resultSet, rowNumber) -> new CompanyPostingCount(
                        resultSet.getLong("company_id"),
                        resultSet.getLong("posting_count")
                ))
                .list();
    }

    public List<CompanySkillCount> findSkillCounts(Collection<Long> companyIds) {
        return jdbcClient.sql("""
                        select posting.company_id,
                               skill.id as skill_id,
                               skill.name as skill_name,
                               count(*) as posting_count
                        from market.job_posting posting
                        join market.posting_skill posting_skill
                          on posting_skill.posting_id = posting.id
                        join market.skill skill
                          on skill.id = posting_skill.skill_id
                        where posting.company_id in (:companyIds)
                        group by posting.company_id, skill.id, skill.name
                        order by posting.company_id, posting_count desc, skill.name asc
                        """)
                .param("companyIds", companyIds)
                .query((resultSet, rowNumber) -> new CompanySkillCount(
                        resultSet.getLong("company_id"),
                        resultSet.getInt("skill_id"),
                        resultSet.getString("skill_name"),
                        resultSet.getLong("posting_count")
                ))
                .list();
    }

    public record CompanyPostingCount(Long companyId, long postingCount) {
    }

    public record CompanySkillCount(Long companyId, Integer skillId, String skillName, long postingCount) {
    }
}
