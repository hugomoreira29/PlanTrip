package com.plantrip.api.weather;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DayWeatherResponse(LocalDate date, BigDecimal maxTemperature, BigDecimal minTemperature,
                                 Integer weatherCode) {


    public static DayWeatherResponse from(OpenMeteoResponse response) {
        OpenMeteoDaily daily = response.daily();
        return new DayWeatherResponse(daily.time().get(0), daily.maxTemperatures().get(0), daily.minTemperatures().get(0), daily.weatherCodes().get(0));
    }
}
