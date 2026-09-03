package com.example.devs.domain.company.presentation;

import com.example.devs.domain.company.presentation.dto.response.CompanyResponse;
import com.example.devs.domain.company.service.CompanyAllQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {
    private final CompanyAllQueryService companyAllQueryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CompanyResponse> getAllCompany() {
        return companyAllQueryService.execute();
    }
}
