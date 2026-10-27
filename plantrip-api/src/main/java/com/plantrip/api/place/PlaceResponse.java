package com.plantrip.api.place;

public record PlaceResponse(Long id, Long dayId, String name, String description) {
    public static PlaceResponse from(Place place) {
        return new PlaceResponse(place.getId(), place.getDay().getId(), place.getName(), place.getDescription());
    }
}
