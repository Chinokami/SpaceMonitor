package com.devda.spacemonitor.measurement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByAstronautIdAndIndicatorIdOrderByMeasuredAtDesc(
            Long astronautId, Long indicatorId);

    List<Measurement> findTop20ByAstronautIdAndIndicatorIdOrderByMeasuredAtDesc(
            Long astronautId, Long indicatorId);

    @Modifying
    @Query(value = "update measurements set astronaut_id = :newId where astronaut_id = :oldId", nativeQuery = true)
    int reassignAstronaut(@Param("oldId") Long oldId, @Param("newId") Long newId);
}
