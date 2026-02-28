package com.weather.sensorapi.dto;

import com.weather.sensorapi.enums.Metric;
import com.weather.sensorapi.enums.Statistic;

import java.time.LocalDate;
import java.util.List;

public record QueryRequest(
        List<String> sensorIds,
        List<Metric> metrics,
        Statistic statistic,
        LocalDate from,
        LocalDate to
) {}
