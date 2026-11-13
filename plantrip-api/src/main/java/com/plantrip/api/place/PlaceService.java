package com.plantrip.api.place;

import com.plantrip.api.day.Day;
import com.plantrip.api.day.DayRepository;
import com.plantrip.api.geocoding.GeocodingService;
import com.plantrip.api.geocoding.NominatimResult;
import com.plantrip.api.route.OsrmResponse;
import com.plantrip.api.route.RouteService;
import com.plantrip.api.trip.TripRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final DayRepository dayRepository;
    private final GeocodingService geocodingService;
    private final TripRepository tripRepository;
    private final RouteService routeService;

    public PlaceService(PlaceRepository placeRepository, DayRepository dayRepository, GeocodingService geocodingService, TripRepository tripRepository, RouteService routeService) {
        this.placeRepository = placeRepository;
        this.dayRepository = dayRepository;
        this.geocodingService = geocodingService;
        this.tripRepository = tripRepository;
        this.routeService = routeService;
    }

    public List<PlaceResponse> findAllByDay(Long dayId, String email) {
        Day day = dayRepository.findById(dayId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"));
        if (!tripRepository.canAccess(day.getTrip().getId(), email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found");
        }
        return placeRepository.findByDayIdOrderByIdAsc(dayId)
                .stream()
                .map(PlaceResponse::from)
                .toList();
    }

    public PlaceResponse create(Long dayId, PlaceRequest placeRequest, String email) {
        Day day = dayRepository.findById(dayId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"));
        if (!tripRepository.canAccess(day.getTrip().getId(), email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found");
        }
        Place place = new Place(day, placeRequest.name(), placeRequest.description());
        applyCoordinates(place);
        Place saved = placeRepository.save(place);
        return PlaceResponse.from(saved);
    }

    public void delete(Long placeId, String email) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found"));
        if (!tripRepository.canAccess(place.getDay().getTrip().getId(), email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        placeRepository.delete(place);
    }

    public PlaceResponse update(Long placeId, PlaceRequest placeRequest, String email) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found"));
        if (!tripRepository.canAccess(place.getDay().getTrip().getId(), email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        place.setName(placeRequest.name());
        place.setDescription(placeRequest.description());
        applyCoordinates(place);
        Place saved = placeRepository.save(place);
        return PlaceResponse.from(saved);
    }

    private void applyCoordinates(Place place) {
        NominatimResult[] results = geocodingService.search(place.getName());
        if (results.length > 0) {
            NominatimResult firstResult = results[0];
            BigDecimal latitude = new BigDecimal(firstResult.lat());
            BigDecimal longitude = new BigDecimal(firstResult.lon());
            place.setLatitude(latitude);
            place.setLongitude(longitude);
        } else {
            place.setLatitude(null);
            place.setLongitude(null);
        }
    }

    public void optimizeRoute(Long dayId, String email) {
        Day day = dayRepository.findById(dayId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"));
        if (!tripRepository.canAccess(day.getTrip().getId(), email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found");
        }
        List<Place> withCoords = day.getPlaces().stream()
                .filter(p -> p.getLatitude() != null)
                .toList();
        if (withCoords.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Need at least 2 places with coordinates");
        }
        OsrmResponse response = routeService.optimize(withCoords);
        for (int i = 0; i < withCoords.size(); i++) {
            Place place = withCoords.get(i);
            Integer position = response.waypoints().get(i).waypointIndex();
            place.setPosition(position);
        }
        placeRepository.saveAll(withCoords);
    }
}
