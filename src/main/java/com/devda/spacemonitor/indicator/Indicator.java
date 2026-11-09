package com.devda.spacemonitor.indicator;

import jakarta.persistence.*;

@Entity
@Table(name = "indicators")
public class Indicator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 60)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 30)
    private String unit;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IndicatorDataType dataType;

    // Демонстраційні орієнтири, не медичні пороги.
    private Double minNormal;
    private Double maxNormal;

    // Межі лише для генератора демонстраційних даних.
    private Double simulationMin;
    private Double simulationMax;

    protected Indicator() {}

    public Indicator(String code, String name, String unit, String description,
                     IndicatorDataType dataType, Double minNormal, Double maxNormal,
                     Double simulationMin, Double simulationMax) {
        this.code = code;
        this.name = name;
        this.unit = unit;
        this.description = description;
        this.dataType = dataType;
        this.minNormal = minNormal;
        this.maxNormal = maxNormal;
        this.simulationMin = simulationMin;
        this.simulationMax = simulationMax;
    }

    public Long getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getUnit() { return unit; }
    public String getDescription() { return description; }
    public IndicatorDataType getDataType() { return dataType; }
    public Double getMinNormal() { return minNormal; }
    public Double getMaxNormal() { return maxNormal; }
    public Double getSimulationMin() { return simulationMin; }
    public Double getSimulationMax() { return simulationMax; }
}
