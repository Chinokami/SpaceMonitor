package com.devda.spacemonitor.api.dto;

import com.devda.spacemonitor.indicator.IndicatorDataType;
import com.devda.spacemonitor.indicator.IndicatorStatus;
import java.util.List;

public record IndicatorStatusDto(
        Long indicatorId,
        String code,
        String name,
        String unit,
        String description,
        IndicatorDataType dataType,
        IndicatorStatus status,
        MeasurementDto latestMeasurement,
        List<MeasurementDto> history
) {}
