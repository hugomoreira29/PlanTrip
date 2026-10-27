package com.plantrip.api.trip;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TripRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 100) String destination,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate) {
}
