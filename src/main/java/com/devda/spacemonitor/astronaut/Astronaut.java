package com.devda.spacemonitor.astronaut;

import jakarta.persistence.*;

/** Internal technical record used as a foreign-key owner for measurements.
 * The actual user's profile is stored locally by the mobile frontend.
 */
@Entity
@Table(name = "astronauts")
public class Astronaut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Legacy columns are retained for compatibility with existing PostgreSQL databases.
    @Column(nullable = false, length = 100)
    private String name = "Internal data owner";
    private Double height = 0.0;
    private Double weight = 0.0;
    @Column(name = "day_streak", nullable = false)
    private Integer dayStreak = 0;

    protected Astronaut() {}

    public Astronaut(String name, Double height, Double weight, Integer dayStreak) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.dayStreak = dayStreak == null ? 0 : dayStreak;
    }

    public Long getId() { return id; }
}
