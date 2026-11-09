package com.devda.spacemonitor.seed;

import com.devda.spacemonitor.astronaut.*;
import com.devda.spacemonitor.indicator.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final AstronautRepository astronautRepository;
    private final IndicatorRepository indicatorRepository;

    public DataInitializer(AstronautRepository astronautRepository,
                           IndicatorRepository indicatorRepository) {
        this.astronautRepository = astronautRepository;
        this.indicatorRepository = indicatorRepository;
    }

    @Override
    public void run(String... args) {
        if (astronautRepository.count() == 0) {
            astronautRepository.saveAll(List.of(
                    new Astronaut("Astronaut 1"), new Astronaut("Astronaut 2"),
                    new Astronaut("Astronaut 3"), new Astronaut("Astronaut 4")));
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
                    "Рівень глюкози в крові. Демонстраційні дані.",
                    IndicatorDataType.NUMERIC, 70.0, 140.0, 75.0, 155.0),
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
                    "Зображення з наданого датасету. Файли покласти в data/images.",
                    IndicatorDataType.IMAGE, null, null, null, null)
            ));
        }
    }
}
