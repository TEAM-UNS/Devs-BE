package com.example.devs.domain.report.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportQueryRepository {

    private final JdbcClient jdbcClient;

    public List<TechStackCount> findPopularTechStacks(
            Integer majorId,
            OffsetDateTime previousStart,
            OffsetDateTime currentStart,
            OffsetDateTime currentEnd,
            int limit
    ) {
        return jdbcClient.sql("""
                  select skill.id as tech_stack_id,
                         skill.name,
                         count(distinct posting.id) filter (
                             where posting.posted_at >= :currentStart
                               and posting.posted_at < :currentEnd
                         ) as current_count,
                         count(distinct posting.id) filter (
                             where posting.posted_at >= :previousStart
                               and posting.posted_at < :currentStart
                         ) as previous_count
                  from market.posting_skill posting_skill
                  join market.skill skill
                    on skill.id = posting_skill.skill_id
                  join market.job_posting posting
                    on posting.id = posting_skill.posting_id
                  where posting.field_id = :majorId
                    and posting.posted_at >= :previousStart
                    and posting.posted_at < :currentEnd
                  group by skill.id, skill.name
                  having count(distinct posting.id) filter (
                      where posting.posted_at >= :currentStart
                        and posting.posted_at < :currentEnd
                  ) > 0
                  order by current_count desc,
                           skill.name asc
                  limit :limit
                  """)
                .param("majorId", majorId)
                .param("previousStart", previousStart)
                .param("currentStart", currentStart)
                .param("currentEnd", currentEnd)
                .param("limit", limit)
                .query((resultSet, rowNumber) -> new TechStackCount(
                        resultSet.getInt("tech_stack_id"),
                        resultSet.getString("name"),
                        resultSet.getLong("current_count"),
                        resultSet.getLong("previous_count")
                ))
                .list();
    }

    public record TechStackCount(
            Integer techStackId,
            String name,
            long currentCount,
            long previousCount
    ) {
    }
}
