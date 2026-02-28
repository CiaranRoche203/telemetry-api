package com.weather.sensorapi.repository;

import com.weather.sensorapi.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {
}
