package com.devda.spacemonitor.api;

import com.devda.spacemonitor.api.dto.IndicatorStatusDto;
import com.devda.spacemonitor.service.MonitoringService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MonitoringController {
    private final MonitoringService monitoringService;

    public MonitoringController(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    /* Returns all available indicators and their latest measurements. */
    @GetMapping("/indicators")
    public List<IndicatorStatusDto> getIndicators() {
        return monitoringService.getAllIndicators();
    }

    /* Returns an indicator, its history, and the status calculated from graded_values. */
    @GetMapping("/indicators/{indicatorId}/status")
    public IndicatorStatusDto getIndicatorStatus(@PathVariable Long indicatorId) {
        return monitoringService.getIndicatorStatus(indicatorId);
    }

    /* Returns one indicator and its history (same response includes its status). */
    @GetMapping("/indicators/{indicatorId}")
    public IndicatorStatusDto getIndicator(@PathVariable Long indicatorId) {
        return monitoringService.getIndicatorStatus(indicatorId);
    }
}
