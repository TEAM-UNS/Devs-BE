package com.example.devs.domain.tech_field.service;

import com.example.devs.domain.skill.domain.SkillField;
import com.example.devs.domain.skill.domain.repository.SkillFieldRepository;
import com.example.devs.domain.tech_field.domain.TechField;
import com.example.devs.domain.tech_field.domain.repository.TechFieldRepository;
import com.example.devs.domain.tech_field.presentation.dto.response.MajorListResponse;
import com.example.devs.domain.tech_field.presentation.dto.response.MajorResponse;
import com.example.devs.domain.tech_field.presentation.dto.response.TechStackResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MajorQueryService {
    private final TechFieldRepository techFieldRepository;
    private final SkillFieldRepository skillFieldRepository;

    @Transactional(readOnly = true)
    public MajorListResponse execute() {
        List<TechField> fields = techFieldRepository.findAllByOrderBySortOrderAsc();
        List<Integer> fieldIds = fields.stream()
                .map(TechField::getId)
                .toList();

        Map<Integer, List<TechStackResponse>> techStacksByField = fieldIds.isEmpty()
                ? Map.of()
                : skillFieldRepository.findAllWithSkillByFieldIds(fieldIds).stream()
                        .collect(Collectors.groupingBy(
                                skillField -> skillField.getId().getFieldId(),
                                Collectors.mapping(this::toTechStackResponse, Collectors.toList())
                        ));

        List<MajorResponse> categories = fields.stream()
                .map(field -> new MajorResponse(
                        field.getId(),
                        field.getCode().toUpperCase(Locale.ROOT),
                        techStacksByField.getOrDefault(field.getId(), List.of())
                ))
                .toList();

        return new MajorListResponse(categories);
    }

    private TechStackResponse toTechStackResponse(SkillField skillField) {
        return new TechStackResponse(
                skillField.getSkill().getId(),
                skillField.getSkill().getName()
        );
    }
}
