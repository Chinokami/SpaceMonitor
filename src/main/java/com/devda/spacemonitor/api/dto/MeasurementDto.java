package com.devda.spacemonitor.api.dto;

import java.time.LocalDateTime;

public record MeasurementDto(
        Long id, Double value, Double secondaryValue, String imageUrl,
        LocalDateTime measuredAt
) {}
