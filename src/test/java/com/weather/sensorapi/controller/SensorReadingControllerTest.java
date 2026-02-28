package com.weather.sensorapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.sensorapi.dto.SensorReadingRequest;
import com.weather.sensorapi.enums.Metric;
import com.weather.sensorapi.model.SensorReading;
import com.weather.sensorapi.service.SensorReadingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorReadingController.class)
class SensorReadingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SensorReadingService service;

    @Test
    void record_withValidRequest_returns201() throws Exception {
        SensorReadingRequest request = new SensorReadingRequest(Metric.TEMPERATURE, 23.5, null);
        SensorReading reading = SensorReading.builder()
                .id(1L)
                .sensorId("sensor-1")
                .metric(Metric.TEMPERATURE)
                .value(23.5)
                .recordedAt(LocalDateTime.now())
                .build();

        when(service.record(eq("sensor-1"), any())).thenReturn(reading);

        mockMvc.perform(post("/api/sensors/sensor-1/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sensorId").value("sensor-1"))
                .andExpect(jsonPath("$.metric").value("TEMPERATURE"))
                .andExpect(jsonPath("$.value").value(23.5));
    }

    @Test
    void record_withMissingMetric_returns400() throws Exception {
        String invalidJson = "{\"value\": 23.5}";

        mockMvc.perform(post("/api/sensors/sensor-1/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void record_withMissingValue_returns400() throws Exception {
        String invalidJson = "{\"metric\": \"TEMPERATURE\"}";

        mockMvc.perform(post("/api/sensors/sensor-1/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void record_withInvalidMetric_returns400() throws Exception {
        String invalidJson = "{\"metric\": \"INVALID\", \"value\": 23.5}";

        mockMvc.perform(post("/api/sensors/sensor-1/readings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
