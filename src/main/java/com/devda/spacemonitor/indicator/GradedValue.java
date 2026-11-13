package com.devda.spacemonitor.indicator;

import jakarta.persistence.*;

/** A configurable value interval for one indicator status.
 * Multiple intervals per status are allowed (for example, both low and high values can be BAD).
 */
@Entity
@Table(name = "graded_values", indexes = {
        @Index(name = "idx_graded_values_indicator", columnList = "indicator_id")
})
public class GradedValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "indicator_id", nullable = false)
    private Indicator indicator;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IndicatorStatus status;

    @Column(name = "min_value")
    private Double minValue;

    @Column(name = "max_value")
    private Double maxValue;

    @Column(name = "min_secondary_value")
    private Double minSecondaryValue;

    @Column(name = "max_secondary_value")
    private Double maxSecondaryValue;

    protected GradedValue() {}

    public GradedValue(Indicator indicator, IndicatorStatus status,
                       Double minValue, Double maxValue,
                       Double minSecondaryValue, Double maxSecondaryValue) {
        this.indicator = indicator;
        this.status = status;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minSecondaryValue = minSecondaryValue;
        this.maxSecondaryValue = maxSecondaryValue;
    }

    public Long getId() { return id; }
    public Indicator getIndicator() { return indicator; }
    public IndicatorStatus getStatus() { return status; }
    public Double getMinValue() { return minValue; }
    public Double getMaxValue() { return maxValue; }
    public Double getMinSecondaryValue() { return minSecondaryValue; }
    public Double getMaxSecondaryValue() { return maxSecondaryValue; }
}
