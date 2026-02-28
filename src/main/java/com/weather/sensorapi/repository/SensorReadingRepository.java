package com.weather.sensorapi.repository;

import com.weather.sensorapi.enums.Metric;
import com.weather.sensorapi.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    // AVERAGE
    @Query("SELECT r.sensorId, r.metric, AVG(r.value) FROM SensorReading r WHERE r.metric IN :metrics AND r.recordedAt BETWEEN :from AND :to GROUP BY r.sensorId, r.metric")
    List<Object[]> findAvgAllSensors(@Param("metrics") List<Metric> metrics, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT r.sensorId, r.metric, AVG(r.value) FROM SensorReading r WHERE r.sensorId IN :sensorIds AND r.metric IN :metrics AND r.recordedAt BETWEEN :from AND :to GROUP BY r.sensorId, r.metric")
    List<Object[]> findAvgBySensors(@Param("sensorIds") List<String> sensorIds, @Param("metrics") List<Metric> metrics, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    // MIN
    @Query("SELECT r.sensorId, r.metric, MIN(r.value) FROM SensorReading r WHERE r.metric IN :metrics AND r.recordedAt BETWEEN :from AND :to GROUP BY r.sensorId, r.metric")
    List<Object[]> findMinAllSensors(@Param("metrics") List<Metric> metrics, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT r.sensorId, r.metric, MIN(r.value) FROM SensorReading r WHERE r.sensorId IN :sensorIds AND r.metric IN :metrics AND r.recordedAt BETWEEN :from AND :to GROUP BY r.sensorId, r.metric")
    List<Object[]> findMinBySensors(@Param("sensorIds") List<String> sensorIds, @Param("metrics") List<Metric> metrics, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    // MAX
    @Query("SELECT r.sensorId, r.metric, MAX(r.value) FROM SensorReading r WHERE r.metric IN :metrics AND r.recordedAt BETWEEN :from AND :to GROUP BY r.sensorId, r.metric")
    List<Object[]> findMaxAllSensors(@Param("metrics") List<Metric> metrics, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT r.sensorId, r.metric, MAX(r.value) FROM SensorReading r WHERE r.sensorId IN :sensorIds AND r.metric IN :metrics AND r.recordedAt BETWEEN :from AND :to GROUP BY r.sensorId, r.metric")
    List<Object[]> findMaxBySensors(@Param("sensorIds") List<String> sensorIds, @Param("metrics") List<Metric> metrics, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    // SUM
    @Query("SELECT r.sensorId, r.metric, SUM(r.value) FROM SensorReading r WHERE r.metric IN :metrics AND r.recordedAt BETWEEN :from AND :to GROUP BY r.sensorId, r.metric")
    List<Object[]> findSumAllSensors(@Param("metrics") List<Metric> metrics, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);

    @Query("SELECT r.sensorId, r.metric, SUM(r.value) FROM SensorReading r WHERE r.sensorId IN :sensorIds AND r.metric IN :metrics AND r.recordedAt BETWEEN :from AND :to GROUP BY r.sensorId, r.metric")
    List<Object[]> findSumBySensors(@Param("sensorIds") List<String> sensorIds, @Param("metrics") List<Metric> metrics, @Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
