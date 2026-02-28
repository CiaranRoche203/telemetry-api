package com.weather.sensorapi.controller;

import com.weather.sensorapi.dto.QueryResult;
import com.weather.sensorapi.service.SensorQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SensorQueryController.class)
class SensorQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SensorQueryService service;

    @Test
    void query_withValidParams_returns200() throws Exception {
        when(service.query(any())).thenReturn(List.of(
                new QueryResult("sensor-1", "TEMPERATURE", "AVERAGE", 22.5)
        ));

        mockMvc.perform(get("/api/sensors/query")
                        .param("metrics", "TEMPERATURE")
                        .param("statistic", "AVERAGE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sensorId").value("sensor-1"))
                .andExpect(jsonPath("$[0].metric").value("TEMPERATURE"))
                .andExpect(jsonPath("$[0].value").value(22.5));
    }

    @Test
    void query_withMultipleMetrics_returns200() throws Exception {
        when(service.query(any())).thenReturn(List.of(
                new QueryResult("sensor-1", "TEMPERATURE", "MIN", 18.0),
                new QueryResult("sensor-1", "HUMIDITY", "MIN", 55.0)
        ));

        mockMvc.perform(get("/api/sensors/query")
                        .param("metrics", "TEMPERATURE", "HUMIDITY")
                        .param("statistic", "MIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void query_withInvalidMetric_returns400() throws Exception {
        mockMvc.perform(get("/api/sensors/query")
                        .param("metrics", "INVALID_METRIC")
                        .param("statistic", "AVERAGE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void query_withMissingStatistic_returns400() throws Exception {
        mockMvc.perform(get("/api/sensors/query")
                        .param("metrics", "TEMPERATURE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void query_withMissingMetrics_returns400() throws Exception {
        mockMvc.perform(get("/api/sensors/query")
                        .param("statistic", "AVERAGE"))
                .andExpect(status().isBadRequest());
    }
}
