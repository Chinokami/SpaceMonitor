package com.devda.spacemonitor.seed;

import com.devda.spacemonitor.astronaut.*;
import com.devda.spacemonitor.indicator.*;
import com.devda.spacemonitor.measurement.MeasurementRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final AstronautRepository astronautRepository;
    private final IndicatorRepository indicatorRepository;
    private final MeasurementRepository measurementRepository;

    public DataInitializer(AstronautRepository astronautRepository,
                           IndicatorRepository indicatorRepository,
                           MeasurementRepository measurementRepository) {
        this.astronautRepository = astronautRepository;
        this.indicatorRepository = indicatorRepository;
        this.measurementRepository = measurementRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // Один технічний запис потрібен лише для зв'язку вимірювань у БД.
        List<Astronaut> astronauts = astronautRepository.findAll();
        Astronaut keeper;
        if (astronauts.isEmpty()) {
            keeper = astronautRepository.save(new Astronaut("Internal data owner", 0.0, 0.0, 0));
        } else {
            keeper = astronauts.getFirst();
            for (int i = 1; i < astronauts.size(); i++) {
                Astronaut duplicate = astronauts.get(i);
                measurementRepository.reassignAstronaut(duplicate.getId(), keeper.getId());
                astronautRepository.delete(duplicate);
            }
        }

        if (indicatorRepository.count() == 0) {
            indicatorRepository.saveAll(List.of(
                new Indicator("HEART_RATE", "Heart rate", "bpm",
                    "Частота серцевих скорочень.", IndicatorDataType.NUMERIC,
                    60.0, 100.0, 58.0, 105.0),
                new Indicator("BLOOD_PRESSURE", "Blood pressure", "mmHg",
                    "Артеріальний тиск: value — систолічний, secondaryValue — діастолічний.",
                    IndicatorDataType.PAIR, 90.0, 120.0, null, null),
                new Indicator("BLOOD_GLUCOSE", "Blood glucose", "mg/dL",
                    "Рівень глюкози в крові.", IndicatorDataType.NUMERIC,
                    70.0, 140.0, 75.0, 155.0),
                new Indicator("BODY_TEMPERATURE", "Body temperature", "°C",
                    "Температура тіла.", IndicatorDataType.NUMERIC,
                    36.1, 37.2, 36.0, 37.6),
                new Indicator("SLEEP_DURATION", "Sleep duration", "hours",
                    "Тривалість сну за останню добу.", IndicatorDataType.NUMERIC,
                    7.0, 9.0, 4.0, 10.0),
                new Indicator("MOOD", "Mood", "1-10",
                    "Самооцінка настрою від 1 до 10.", IndicatorDataType.NUMERIC,
                    4.0, 10.0, 1.0, 10.0),
                new Indicator("MEDICAL_IMAGE", "Medical image", null,
                    "Медичні зображення для демонстрації.",
                    IndicatorDataType.IMAGE, null, null, null, null)
            ));
        }
    }
}
