package com.weather.sensorapi.dto;

public record QueryResult(
        String sensorId,
        String metric,
        String statistic,
        Double value
) {}
