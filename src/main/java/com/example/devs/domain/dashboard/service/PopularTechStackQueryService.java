package com.example.devs.domain.dashboard.service;

import com.example.devs.domain.dashboard.domain.repository.DashboardQueryRepository;
import com.example.devs.domain.dashboard.presentation.dto.response.PopularTechStackListResponse;
import com.example.devs.domain.dashboard.presentation.dto.response.PopularTechStackResponse;
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
public class PopularTechStackQueryService {
    private static final int POPULAR_TECH_STACK_LIMIT = 9;

    private final DashboardQueryRepository dashboardQueryRepository;
    private final TechFieldRepository techFieldRepository;

    @Transactional(readOnly = true)
    public PopularTechStackListResponse execute(Integer majorId) {
        TechField major = majorId == null
                ? null
                : techFieldRepository.findById(majorId)
                        .orElseThrow(MajorNotFoundException::new);

        List<DashboardQueryRepository.PopularTechStack> popularTechStacks = major == null
                ? dashboardQueryRepository.findPopularTechStacks(POPULAR_TECH_STACK_LIMIT)
                : dashboardQueryRepository.findPopularTechStacksByMajorId(
                        major.getId(),
                        POPULAR_TECH_STACK_LIMIT
                );

        List<PopularTechStackResponse> responses = IntStream.range(0, popularTechStacks.size())
                .mapToObj(index -> toResponse(index + 1, popularTechStacks.get(index)))
                .toList();

        String majorCode = major == null
                ? null
                : major.getCode().toUpperCase(Locale.ROOT);

        return new PopularTechStackListResponse(majorCode, responses);
    }

    private PopularTechStackResponse toResponse(
            int rank,
            DashboardQueryRepository.PopularTechStack techStack
    ) {
        return new PopularTechStackResponse(
                rank,
                techStack.id(),
                techStack.name(),
                techStack.count()
        );
    }
}
