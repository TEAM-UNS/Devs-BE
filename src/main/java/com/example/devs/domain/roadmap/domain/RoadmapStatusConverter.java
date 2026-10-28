package com.example.devs.domain.roadmap.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoadmapStatusConverter implements AttributeConverter<RoadmapStatus, String> {
    @Override
    public String convertToDatabaseColumn(RoadmapStatus status) {
        return status == null ? null : status.databaseValue();
    }

    @Override
    public RoadmapStatus convertToEntityAttribute(String databaseValue) {
        return databaseValue == null ? null : RoadmapStatus.from(databaseValue);
    }
}
