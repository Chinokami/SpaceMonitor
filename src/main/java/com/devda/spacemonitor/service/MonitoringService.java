package com.devda.spacemonitor.service;

import com.devda.spacemonitor.api.dto.IndicatorStatusDto;
import com.devda.spacemonitor.api.dto.MeasurementDto;
import com.devda.spacemonitor.astronaut.Astronaut;
import com.devda.spacemonitor.astronaut.AstronautRepository;
import com.devda.spacemonitor.indicator.*;
import com.devda.spacemonitor.measurement.Measurement;
import com.devda.spacemonitor.measurement.MeasurementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MonitoringService {
    private final AstronautRepository astronautRepository;
    private final IndicatorRepository indicatorRepository;
    private final GradedValueRepository gradedValueRepository;
    private final MeasurementRepository measurementRepository;
    private final ImageDatasetService imageDatasetService;

    public MonitoringService(AstronautRepository astronautRepository,
                             IndicatorRepository indicatorRepository,
                             GradedValueRepository gradedValueRepository,
                             MeasurementRepository measurementRepository,
                             ImageDatasetService imageDatasetService) {
        this.astronautRepository = astronautRepository;
        this.indicatorRepository = indicatorRepository;
        this.gradedValueRepository = gradedValueRepository;
        this.measurementRepository = measurementRepository;
        this.imageDatasetService = imageDatasetService;
    }

    @Transactional(readOnly = true)
    public List<IndicatorStatusDto> getAllIndicators() {
        Astronaut astronaut = getInternalAstronaut();
        return indicatorRepository.findAll().stream()
                .map(indicator -> buildIndicatorStatusDto(indicator, astronaut.getId(), true))
                .toList();
    }

    @Transactional(readOnly = true)
    public IndicatorStatusDto getIndicatorStatus(Long indicatorId) {
        Indicator indicator = indicatorRepository.findById(indicatorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Індикатор не знайдено"));
        Astronaut astronaut = getInternalAstronaut();
        return buildIndicatorStatusDto(indicator, astronaut.getId(), false);
    }

    private IndicatorStatusDto buildIndicatorStatusDto(Indicator indicator, Long astronautId, boolean limitHistory) {
        List<Measurement> measurements = limitHistory
                ? measurementRepository.findTop20ByAstronautIdAndIndicatorIdOrderByMeasuredAtDesc(astronautId, indicator.getId())
                : measurementRepository.findByAstronautIdAndIndicatorIdOrderByMeasuredAtDesc(astronautId, indicator.getId());

        List<MeasurementDto> history = measurements.stream()
                .map(this::toMeasurementDto)
                .toList();

        MeasurementDto latest = history.isEmpty() ? null : history.getFirst();
        IndicatorStatus status = calculateStatus(indicator, latest);

        return new IndicatorStatusDto(
                indicator.getId(), indicator.getCode(), indicator.getName(),
                indicator.getUnit(), indicator.getDescription(), indicator.getDataType(),
                status, latest, history);
    }

    private IndicatorStatus calculateStatus(Indicator indicator, MeasurementDto measurement) {
        if (measurement == null || indicator.getDataType() == IndicatorDataType.IMAGE
                || measurement.value() == null) {
            return IndicatorStatus.UNKNOWN;
        }

        List<GradedValue> ranges = gradedValueRepository.findAllByIndicatorId(indicator.getId())
                .stream()
                .sorted(Comparator.comparingInt(r -> statusPriority(r.getStatus())))
                .toList();

        for (GradedValue range : ranges) {
            if (matches(measurement.value(), measurement.secondaryValue(), range)) {
                return range.getStatus();
            }
        }
        return IndicatorStatus.UNKNOWN;
    }

    private int statusPriority(IndicatorStatus status) {
        return switch (status) {
            case BAD -> 1;
            case NORMAL -> 2;
            case GOOD -> 3;
            case UNKNOWN -> 4;
        };
    }

    private boolean matches(Double value, Double secondaryValue, GradedValue range) {
        boolean hasPrimaryBounds = range.getMinValue() != null || range.getMaxValue() != null;
        if (hasPrimaryBounds && !inside(value, range.getMinValue(), range.getMaxValue())) {
            return false;
        }

        boolean hasSecondaryBounds = range.getMinSecondaryValue() != null
                || range.getMaxSecondaryValue() != null;
        if (!hasSecondaryBounds) return true;
        return secondaryValue != null && inside(secondaryValue,
                range.getMinSecondaryValue(), range.getMaxSecondaryValue());
    }

    private boolean inside(Double value, Double min, Double max) {
        return value != null && (min == null || value >= min) && (max == null || value <= max);
    }

    private Astronaut getInternalAstronaut() {
        return astronautRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR, "Не створено внутрішній запис астронавта"));
    }

    @Transactional
    public void updateInfo() {
        Astronaut astronaut = getInternalAstronaut();
        for (Indicator indicator : indicatorRepository.findAll()) {
            Measurement measurement = generateMeasurement(astronaut, indicator);
            if (measurement != null) measurementRepository.save(measurement);
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
            return new Measurement(astronaut, indicator, round1(systolic), round1(diastolic), null);
        }

        double value = random.nextDouble(indicator.getSimulationMin(),
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
