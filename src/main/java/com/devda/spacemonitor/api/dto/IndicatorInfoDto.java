package com.devda.spacemonitor.api.dto;

import com.devda.spacemonitor.indicator.IndicatorDataType;
import java.util.List;

public record IndicatorInfoDto(
        Long astronautId, String astronautName,
        Long indicatorId, String code, String name, String unit, String description,
        IndicatorDataType dataType, Double minNormal, Double maxNormal,
        MeasurementDto latestMeasurement, List<MeasurementDto> history
) {}
