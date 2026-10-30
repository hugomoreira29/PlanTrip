package com.plantrip.api.place;

import com.plantrip.api.day.Day;
import com.plantrip.api.day.DayRepository;
import com.plantrip.api.geocoding.GeocodingService;
import com.plantrip.api.geocoding.NominatimResult;
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

    public PlaceService(PlaceRepository placeRepository, DayRepository dayRepository, GeocodingService geocodingService) {
        this.placeRepository = placeRepository;
        this.dayRepository = dayRepository;
        this.geocodingService = geocodingService;
    }

    public List<PlaceResponse> findAllByDay(Long dayId) {
        if (!dayRepository.existsById(dayId)) {
            throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found");
        }
        return placeRepository.findByDayIdOrderByIdAsc(dayId)
                .stream()
                .map(PlaceResponse::from)
                .toList();
    }

    public PlaceResponse create(Long dayId, PlaceRequest placeRequest) {
        Day day = dayRepository.findById(dayId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"));
        Place place = new Place(day, placeRequest.name(), placeRequest.description());
        NominatimResult[] results = geocodingService.search(place.getName());
        if (results.length > 0){
            NominatimResult firstResult = results[0];
            BigDecimal latitude = new BigDecimal(firstResult.lat());
            BigDecimal longitude = new BigDecimal(firstResult.lon());
            place.setLatitude(latitude);
            place.setLongitude(longitude);
        }
        Place saved = placeRepository.save(place);
        return PlaceResponse.from(saved);
    }

    public void delete(Long placeId) {
        if (!placeRepository.existsById(placeId)) {
            throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        placeRepository.deleteById(placeId);
    }

    public PlaceResponse update(Long placeId, PlaceRequest placeRequest) {
        Place place = placeRepository.findById(placeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found"));
        place.setName(placeRequest.name());
        place.setDescription(placeRequest.description());
        Place saved = placeRepository.save(place);
        return PlaceResponse.from(saved);
    }
}
