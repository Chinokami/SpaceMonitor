package com.devda.spacemonitor.measurement;

import com.devda.spacemonitor.astronaut.Astronaut;
import com.devda.spacemonitor.indicator.Indicator;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "measurements", indexes = {
    @Index(name = "idx_measurement_astronaut_indicator_time",
           columnList = "astronaut_id, indicator_id, measured_at")
})
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "astronaut_id", nullable = false)
    private Astronaut astronaut;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "indicator_id", nullable = false)
    private Indicator indicator;

    private Double value;
    private Double secondaryValue;

    @Column(length = 500)
    private String imagePath;

    @Column(nullable = false)
    private LocalDateTime measuredAt;

    protected Measurement() {}

    public Measurement(Astronaut astronaut, Indicator indicator, Double value,
                       Double secondaryValue, String imagePath) {
        this.astronaut = astronaut;
        this.indicator = indicator;
        this.value = value;
        this.secondaryValue = secondaryValue;
        this.imagePath = imagePath;
        this.measuredAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Astronaut getAstronaut() { return astronaut; }
    public Indicator getIndicator() { return indicator; }
    public Double getValue() { return value; }
    public Double getSecondaryValue() { return secondaryValue; }
    public String getImagePath() { return imagePath; }
    public LocalDateTime getMeasuredAt() { return measuredAt; }
}
