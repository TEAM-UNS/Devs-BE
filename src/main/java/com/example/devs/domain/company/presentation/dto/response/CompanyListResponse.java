package com.example.devs.domain.company.presentation.dto.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record CompanyListResponse(
        List<CompanyResponse> companies,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) {
    public static CompanyListResponse from(Page<CompanyResponse> companies) {
        return new CompanyListResponse(
                companies.getContent(),
                companies.getNumber(),
                companies.getSize(),
                companies.getTotalElements(),
                companies.getTotalPages(),
                companies.hasNext()
        );
    }
}
