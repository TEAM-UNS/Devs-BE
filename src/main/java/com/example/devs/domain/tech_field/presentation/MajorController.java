package com.example.devs.domain.tech_field.presentation;

import com.example.devs.domain.tech_field.presentation.dto.response.MajorListResponse;
import com.example.devs.domain.tech_field.service.MajorQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/majors")
public class MajorController {
    private final MajorQueryService majorQueryService;

    @GetMapping
    public MajorListResponse getMajors() {
        return majorQueryService.execute();
    }
}
