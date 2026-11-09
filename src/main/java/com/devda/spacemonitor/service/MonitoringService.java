package com.devda.spacemonitor.service;

import com.devda.spacemonitor.api.dto.*;
import com.devda.spacemonitor.astronaut.*;
import com.devda.spacemonitor.indicator.*;
import com.devda.spacemonitor.measurement.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MonitoringService {
    private final AstronautRepository astronautRepository;
    private final IndicatorRepository indicatorRepository;
    private final MeasurementRepository measurementRepository;
    private final ImageDatasetService imageDatasetService;

    public MonitoringService(AstronautRepository astronautRepository,
                             IndicatorRepository indicatorRepository,
                             MeasurementRepository measurementRepository,
                             ImageDatasetService imageDatasetService) {
        this.astronautRepository = astronautRepository;
        this.indicatorRepository = indicatorRepository;
        this.measurementRepository = measurementRepository;
        this.imageDatasetService = imageDatasetService;
    }

    public IndicatorInfoDto getIndicatorInfo(Long astronautId, Long indicatorId) {
        Astronaut astronaut = astronautRepository.findById(astronautId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Астронавта не знайдено"));
        Indicator indicator = indicatorRepository.findById(indicatorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Індикатор не знайдено"));

        List<MeasurementDto> history = measurementRepository
                .findByAstronautIdAndIndicatorIdOrderByMeasuredAtDesc(astronautId, indicatorId)
                .stream().map(this::toMeasurementDto).toList();

        MeasurementDto latest = history.isEmpty() ? null : history.getFirst();
        return new IndicatorInfoDto(
                astronaut.getId(), astronaut.getName(), indicator.getId(),
                indicator.getCode(), indicator.getName(), indicator.getUnit(),
                indicator.getDescription(), indicator.getDataType(),
                indicator.getMinNormal(), indicator.getMaxNormal(), latest, history);
    }

    public List<IndicatorInfoDto> getGeneralStatus(Long astronautId) {
        if (!astronautRepository.existsById(astronautId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Астронавта не знайдено");
        }
        return indicatorRepository.findAll().stream()
                .map(indicator -> getIndicatorInfo(astronautId, indicator.getId()))
                .toList();
    }

    public List<AstronautStatusDto> getGeneralStatusForAllAstronauts() {
        return astronautRepository.findAll().stream()
                .map(astronaut -> new AstronautStatusDto(
                        astronaut.getId(), astronaut.getName(),
                        getGeneralStatus(astronaut.getId())))
                .toList();
    }

    public void updateInfo() {
        for (Astronaut astronaut : astronautRepository.findAll()) {
            for (Indicator indicator : indicatorRepository.findAll()) {
                Measurement measurement = generateMeasurement(astronaut, indicator);
                if (measurement != null) measurementRepository.save(measurement);
            }
        }
    }

    private Measurement generateMeasurement(Astronaut astronaut, Indicator indicator) {
        ThreadLocalRandom random = ThreadLocalRandom.current();

        if (indicator.getDataType() == IndicatorDataType.IMAGE) {
            String imageUrl = imageDatasetService.pickRandomImageUrl();
            if (imageUrl == null) return null;
            return new Measurement(astronaut, indicator, null, null, imageUrl);
        }

        if ("BLOOD_PRESSURE".equals(indicator.getCode())) {
            double systolic = random.nextDouble(100.0, 136.0);
            double diastolic = random.nextDouble(60.0, 86.0);
            return new Measurement(astronaut, indicator, round1(systolic),
                    round1(diastolic), null);
        }

        double value = random.nextDouble(
                indicator.getSimulationMin(),
                Math.nextUp(indicator.getSimulationMax()));

        if ("MOOD".equals(indicator.getCode())) value = Math.round(value);
        return new Measurement(astronaut, indicator, round1(value), null, null);
    }

    private MeasurementDto toMeasurementDto(Measurement measurement) {
        return new MeasurementDto(measurement.getId(), measurement.getValue(),
                measurement.getSecondaryValue(), measurement.getImagePath(),
                measurement.getMeasuredAt());
    }

    private double round1(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
