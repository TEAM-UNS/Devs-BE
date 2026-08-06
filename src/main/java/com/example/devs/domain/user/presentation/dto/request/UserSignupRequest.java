package com.example.devs.domain.user.presentation.dto.request;

import com.example.devs.domain.user.domain.PersonalHistory;
import jakarta.validation.constraints.*;

import java.util.Set;

public record UserSignupRequest(
        @NotBlank(message = "이메일은 필수입니다.")
        @Email(message = "올바른 이메일 형식이어야 합니다.")
        String email,

        @NotBlank(message = "이름은 필수입니다.")
        @Size(min = 2, max = 20, message = "이름은 2자 이상 20자 이하여야 합니다.")
        String name,

        @NotBlank(message = "비밀번호는 필수입니다.")
        @Size(min = 8, max = 64, message = "비밀번호는 8자 이상 64자 이하여야 합니다.")
        String password,

        @NotNull(message = "경력은 필수입니다.")
        PersonalHistory personalHistory,

        @NotEmpty(message = "전공을 한 개 이상 선택해야 합니다.")
        Set<@Positive(message = "전공 ID는 양수여야 합니다.") Long> majorIds,

        @NotEmpty(message = "기술 스택을 한 개 이상 선택해야 합니다.")
        Set<@Positive(message = "기술 스택 ID는 양수여야 합니다.") Long> techStackIds
) {
}
