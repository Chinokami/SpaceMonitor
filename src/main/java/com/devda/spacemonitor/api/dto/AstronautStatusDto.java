package com.devda.spacemonitor.api.dto;

import java.util.List;

public record AstronautStatusDto(
        Long astronautId, String astronautName, List<IndicatorInfoDto> indicators
) {}
