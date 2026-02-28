package com.weather.sensorapi.service;

import com.weather.sensorapi.dto.SensorReadingRequest;
import com.weather.sensorapi.model.SensorReading;
import com.weather.sensorapi.repository.SensorReadingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SensorReadingService {

    private final SensorReadingRepository repository;

    public SensorReading record(String sensorId, SensorReadingRequest request) {
        LocalDateTime timestamp = request.recordedAt() != null ? request.recordedAt() : LocalDateTime.now();
        SensorReading reading = SensorReading.builder()
                .sensorId(sensorId)
                .metric(request.metric())
                .value(request.value())
                .recordedAt(timestamp)
                .build();
        return repository.save(reading);
    }
}
