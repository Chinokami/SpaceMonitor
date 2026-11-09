package com.devda.spacemonitor.scheduler;

import com.devda.spacemonitor.service.MonitoringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MeasurementScheduler {
    private static final Logger log = LoggerFactory.getLogger(MeasurementScheduler.class);
    private final MonitoringService monitoringService;

    public MeasurementScheduler(MonitoringService monitoringService) {
        this.monitoringService = monitoringService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createInitialMeasurements() {
        log.info("Creating initial demo measurements...");
        monitoringService.updateInfo();
    }

    @Scheduled(fixedRate = 120_000, initialDelay = 120_000)
    public void updateMeasurements() {
        log.info("Generating scheduled demo measurements...");
        monitoringService.updateInfo();
    }
}
