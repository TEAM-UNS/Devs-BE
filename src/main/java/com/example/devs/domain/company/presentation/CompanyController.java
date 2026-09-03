package com.example.devs.domain.company.presentation;

import com.example.devs.domain.company.presentation.dto.response.CompanyListResponse;
import com.example.devs.domain.company.service.CompanyAllQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyAllQueryService companyAllQueryService;

    @GetMapping
    public CompanyListResponse getCompanies(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        return companyAllQueryService.execute(keyword, pageable);
    }
}
