package com.plantrip.api.place;

import java.math.BigDecimal;

public record PlaceResponse(Long id, Long dayId, String name, String description, BigDecimal latitude,
                            BigDecimal longitude, Integer position) {
    public static PlaceResponse from(Place place) {
        return new PlaceResponse(place.getId(), place.getDay().getId(), place.getName(), place.getDescription(), place.getLatitude(), place.getLongitude(), place.getPosition());
    }
}
