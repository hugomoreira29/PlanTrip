package com.plantrip.api.day;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DayRequest(
        @NotNull LocalDate dayDate
) {
}
