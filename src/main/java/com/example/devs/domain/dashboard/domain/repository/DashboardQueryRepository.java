package com.example.devs.domain.dashboard.domain.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DashboardQueryRepository {
    private final JdbcClient jdbcClient;

    public long countCollectedBetween(OffsetDateTime start, OffsetDateTime end) {
        return jdbcClient.sql("""
                        select count(*)
                        from market.job_posting
                        where collected_at >= :start
                          and collected_at < :end
                        """)
                .param("start", start)
                .param("end", end)
                .query(Long.class)
                .single();
    }

    public long countActiveCompaniesAt(OffsetDateTime asOf) {
        return jdbcClient.sql("""
                        select count(distinct company_id)
                        from market.job_posting
                        where company_id is not null
                          and collected_at <= :asOf
                          and (expires_at is null or expires_at >= :asOf)
                        """)
                .param("asOf", asOf)
                .query(Long.class)
                .single();
    }

    public Optional<MentionedTech> findMostMentionedTech(
            OffsetDateTime start,
            OffsetDateTime end
    ) {
        return jdbcClient.sql("""
                        select skill.name,
                               count(*) as mention_count
                        from market.posting_skill posting_skill
                        join market.skill skill
                          on skill.id = posting_skill.skill_id
                        join market.job_posting posting
                          on posting.id = posting_skill.posting_id
                        where posting.collected_at >= :start
                          and posting.collected_at < :end
                        group by skill.id, skill.name
                        order by mention_count desc, skill.name asc
                        limit 1
                        """)
                .param("start", start)
                .param("end", end)
                .query((resultSet, rowNumber) -> new MentionedTech(
                        resultSet.getString("name"),
                        resultSet.getLong("mention_count")
                ))
                .optional();
    }

    public Optional<RisingTech> findMostRisingTech(
            OffsetDateTime previousStart,
            OffsetDateTime currentStart,
            OffsetDateTime currentEnd,
            int minimumPreviousCount
    ) {
        return jdbcClient.sql("""
                        with skill_counts as (
                            select skill.id,
                                   skill.name,
                                   count(*) filter (
                                       where posting.collected_at >= :currentStart
                                         and posting.collected_at < :currentEnd
                                   ) as current_count,
                                   count(*) filter (
                                       where posting.collected_at >= :previousStart
                                         and posting.collected_at < :currentStart
                                   ) as previous_count
                            from market.posting_skill posting_skill
                            join market.skill skill
                              on skill.id = posting_skill.skill_id
                            join market.job_posting posting
                              on posting.id = posting_skill.posting_id
                            where posting.collected_at >= :previousStart
                              and posting.collected_at < :currentEnd
                            group by skill.id, skill.name
                        )
                        select name,
                               cast(round(
                                   (current_count - previous_count) * 100.0 / previous_count
                               ) as integer) as growth_rate
                        from skill_counts
                        where previous_count >= :minimumPreviousCount
                          and current_count > previous_count
                        order by growth_rate desc, current_count desc, name asc
                        limit 1
                        """)
                .param("previousStart", previousStart)
                .param("currentStart", currentStart)
                .param("currentEnd", currentEnd)
                .param("minimumPreviousCount", minimumPreviousCount)
                .query((resultSet, rowNumber) -> new RisingTech(
                        resultSet.getString("name"),
                        resultSet.getInt("growth_rate")
                ))
                .optional();
    }

    public List<PopularTechStack> findPopularTechStacks(int limit) {
        return jdbcClient.sql("""
                        select skill.id as tech_stack_id,
                               skill.name,
                               count(*) as posting_count
                        from market.posting_skill posting_skill
                        join market.skill skill
                          on skill.id = posting_skill.skill_id
                        group by skill.id, skill.name
                        order by posting_count desc, skill.name asc
                        limit :limit
                        """)
                .param("limit", limit)
                .query((resultSet, rowNumber) -> new PopularTechStack(
                        resultSet.getInt("tech_stack_id"),
                        resultSet.getString("name"),
                        resultSet.getLong("posting_count")
                ))
                .list();
    }

    public List<PopularTechStack> findPopularTechStacksByMajorId(
            Integer majorId,
            int limit
    ) {
        return jdbcClient.sql("""
                        select skill.id as tech_stack_id,
                               skill.name,
                               count(*) as posting_count
                        from market.posting_skill posting_skill
                        join market.skill skill
                          on skill.id = posting_skill.skill_id
                        join market.job_posting posting
                          on posting.id = posting_skill.posting_id
                        where posting.field_id = :majorId
                        group by skill.id, skill.name
                        order by posting_count desc, skill.name asc
                        limit :limit
                        """)
                .param("majorId", majorId)
                .param("limit", limit)
                .query((resultSet, rowNumber) -> new PopularTechStack(
                        resultSet.getInt("tech_stack_id"),
                        resultSet.getString("name"),
                        resultSet.getLong("posting_count")
                ))
                .list();
    }

    public List<CompanySizeTechStack> findTechStacksByCompanySizeAndMajor(
            Collection<String> companySizes,
            Integer majorId,
            int limit
    ) {
        return jdbcClient.sql("""
                        with target_postings as (
                            select posting.id
                            from market.job_posting posting
                            join market.company company
                              on company.id = posting.company_id
                            where company.size_type in (:companySizes)
                              and posting.field_id = :majorId
                        ),
                        total as (
                            select count(*) as posting_count
                            from target_postings
                        )
                        select skill.name,
                               count(*) as skill_count,
                               cast(round(
                                   count(*) * 100.0 / nullif(total.posting_count, 0)
                               ) as integer) as percentage
                        from target_postings target
                        join market.posting_skill posting_skill
                          on posting_skill.posting_id = target.id
                        join market.skill skill
                          on skill.id = posting_skill.skill_id
                        cross join total
                        group by skill.id, skill.name, total.posting_count
                        order by percentage desc, skill_count desc, skill.name asc
                        limit :limit
                        """)
                .param("companySizes", companySizes)
                .param("majorId", majorId)
                .param("limit", limit)
                .query((resultSet, rowNumber) -> new CompanySizeTechStack(
                        resultSet.getString("name"),
                        resultSet.getInt("percentage")
                ))
                .list();
    }

    public List<TechStackTrendPoint> findTechStackTrendPoints(
            Integer majorId,
            OffsetDateTime start,
            OffsetDateTime end,
            String bucketUnit
    ) {
        return jdbcClient.sql("""
                        select skill.id as tech_stack_id,
                               skill.name,
                               cast(date_trunc(
                                   :bucketUnit,
                                   timezone('Asia/Seoul', posting.posted_at)
                               ) as date) as bucket_date,
                               count(*) as posting_count
                        from market.posting_skill posting_skill
                        join market.skill skill
                          on skill.id = posting_skill.skill_id
                        join market.job_posting posting
                          on posting.id = posting_skill.posting_id
                        where posting.field_id = :majorId
                          and posting.posted_at >= :start
                          and posting.posted_at < :end
                        group by skill.id, skill.name, bucket_date
                        order by skill.id, bucket_date
                        """)
                .param("majorId", majorId)
                .param("start", start)
                .param("end", end)
                .param("bucketUnit", bucketUnit)
                .query((resultSet, rowNumber) -> new TechStackTrendPoint(
                        resultSet.getInt("tech_stack_id"),
                        resultSet.getString("name"),
                        resultSet.getObject("bucket_date", LocalDate.class),
                        resultSet.getLong("posting_count")
                ))
                .list();
    }

    public record MentionedTech(String name, long count) {
    }

    public record RisingTech(String name, int rate) {
    }

    public record PopularTechStack(Integer id, String name, long count) {
    }

    public record CompanySizeTechStack(String name, int percentage) {
    }

    public record TechStackTrendPoint(
            Integer id,
            String name,
            LocalDate date,
            long value
    ) {
    }
}
