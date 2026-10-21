package com.example.devs.domain.dashboard.presentation.dto.response;

public record RisingTechResponse(
        String name,
        int rate
) {
    public static RisingTechResponse of(String name, int rate) {
        return new RisingTechResponse(name, rate);
    }
}
