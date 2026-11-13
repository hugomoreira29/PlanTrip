package com.plantrip.api.weather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OpenMeteoDaily(
        List<LocalDate> time,
        @JsonProperty("temperature_2m_max")
        List<BigDecimal> maxTemperatures,
        @JsonProperty("temperature_2m_min")
        List<BigDecimal> minTemperatures,
        @JsonProperty("weather_code")
        List<Integer> weatherCodes
) {
}
