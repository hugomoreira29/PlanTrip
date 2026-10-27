package com.plantrip.api.trip;

import java.time.LocalDate;
import java.util.List;

public record TripResponse(Long id, String name, String destination, LocalDate startDate, LocalDate endDate) {
    public static TripResponse from(Trip trip) {
        return new TripResponse(trip.getId(), trip.getName(), trip.getDestination(), trip.getStartDate(), trip.getEndDate());
    }
}
