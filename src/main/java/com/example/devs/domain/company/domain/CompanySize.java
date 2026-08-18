package com.example.devs.domain.company.domain;

import java.util.List;

public enum CompanySize {
    STARTUP(List.of("startup")),
    SMALL(List.of("small")),
    MEDIUM(List.of("medium")),
    LARGE(List.of("large", "enterprise"));

    private final List<String> databaseValues;

    CompanySize(List<String> databaseValues) {
        this.databaseValues = databaseValues;
    }

    public List<String> databaseValues() {
        return databaseValues;
    }
}
