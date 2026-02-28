package com.weather.sensorapi.dto;

import com.weather.sensorapi.enums.Metric;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SensorReadingRequest(
        @NotNull(message = "Metric is required") Metric metric,
        @NotNull(message = "Value is required") Double value,
        LocalDateTime recordedAt
) {}
