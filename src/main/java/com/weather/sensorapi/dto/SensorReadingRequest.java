package com.weather.sensorapi.dto;

import com.weather.sensorapi.enums.Metric;

import java.time.LocalDateTime;

public record SensorReadingRequest(
        Metric metric,
        Double value,
        LocalDateTime recordedAt
) {}
