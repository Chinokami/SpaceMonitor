package com.devda.spacemonitor.astronaut;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "astronauts")
public class Astronaut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Astronaut() {}

    public Astronaut(String name) {
        this.name = name;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
