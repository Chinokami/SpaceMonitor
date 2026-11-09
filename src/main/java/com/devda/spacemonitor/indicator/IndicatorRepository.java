package com.devda.spacemonitor.indicator;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface IndicatorRepository extends JpaRepository<Indicator, Long> {
    Optional<Indicator> findByCode(String code);
}
