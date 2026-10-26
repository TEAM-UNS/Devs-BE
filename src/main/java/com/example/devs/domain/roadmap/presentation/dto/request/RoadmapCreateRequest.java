package com.example.devs.domain.roadmap.presentation.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record RoadmapCreateRequest(
        @NotNull(message = "전공을 한 개를 선택하셔야합니다.")
        @Positive(message = "전공 ID는 양수여야 합니다.")
        Integer majorId,

        @NotEmpty(message = "회사를 한 개 이상 선택하셔야합니다.")
        Set<@Positive(message = "회사 ID는 양수여야 합니다.") Long> companyIds,

        @NotEmpty(message = "기술을 한 개 이상 선택해야 합니다.")
        Set<@Positive(message = "기술 ID는 양수여야 합니다.") Integer> skillIds,

        @NotNull(message = "목표 기간을 선택해주세요.")
        @Min(value = 1, message = "목표 기간은 1~12주 사이로 선택해야 합니다.")
        @Max(value = 12, message = "목표 기간은 1~12주 사이로 선택해야 합니다.")
        Integer targetWeeks,

        @Size(max = 500, message = "추가 의견은 500자 이하로 작성해야 합니다.")
        String additionalComment
) {
}
