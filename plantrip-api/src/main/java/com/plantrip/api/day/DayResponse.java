package com.plantrip.api.day;

import java.time.LocalDate;

public record DayResponse(Long id, Long tripId, LocalDate dayDate) {
    public static DayResponse from(Day day) {
        return new DayResponse(day.getId(), day.getTrip().getId(), day.getDayDate());
    }
}
