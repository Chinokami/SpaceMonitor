package com.devda.spacemonitor.indicator;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GradedValueRepository extends JpaRepository<GradedValue, Long> {
    List<GradedValue> findAllByIndicatorId(Long indicatorId);
}
