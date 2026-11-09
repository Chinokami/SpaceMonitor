package com.devda.spacemonitor.api;

import com.devda.spacemonitor.api.dto.*;
import com.devda.spacemonitor.astronaut.AstronautRepository;
import com.devda.spacemonitor.service.MonitoringService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MonitoringController {
    private final MonitoringService monitoringService;
    private final AstronautRepository astronautRepository;

    public MonitoringController(MonitoringService monitoringService,
                                AstronautRepository astronautRepository) {
        this.monitoringService = monitoringService;
        this.astronautRepository = astronautRepository;
    }

    @GetMapping("/astronauts")
    public List<AstronautSummary> getAstronauts() {
        return astronautRepository.findAll().stream()
                .map(a -> new AstronautSummary(a.getId(), a.getName())).toList();
    }

    @GetMapping("/astronauts/{astronautId}/indicators/{indicatorId}")
    public IndicatorInfoDto getIndicatorInfo(@PathVariable Long astronautId,
                                             @PathVariable Long indicatorId) {
        return monitoringService.getIndicatorInfo(astronautId, indicatorId);
    }

    @GetMapping("/astronauts/{astronautId}/status")
    public List<IndicatorInfoDto> getGeneralStatus(@PathVariable Long astronautId) {
        return monitoringService.getGeneralStatus(astronautId);
    }

    @GetMapping("/status")
    public List<AstronautStatusDto> getGeneralStatusForAllAstronauts() {
        return monitoringService.getGeneralStatusForAllAstronauts();
    }

    public record AstronautSummary(Long id, String name) {}
}
