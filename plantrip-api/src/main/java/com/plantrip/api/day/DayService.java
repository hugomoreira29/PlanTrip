package com.plantrip.api.day;

import com.plantrip.api.place.Place;
import com.plantrip.api.trip.Trip;
import com.plantrip.api.trip.TripRepository;
import com.plantrip.api.weather.DayWeatherResponse;
import com.plantrip.api.weather.OpenMeteoResponse;
import com.plantrip.api.weather.WeatherService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DayService {
    private final DayRepository dayRepository;
    private final TripRepository tripRepository;
    private final WeatherService weatherService;

    public DayService(DayRepository dayRepository, TripRepository tripRepository, WeatherService weatherService) {
        this.dayRepository = dayRepository;
        this.tripRepository = tripRepository;
        this.weatherService = weatherService;
    }

    public List<DayResponse> findAllByTrip(Long tripId, String email) {
        if (!tripRepository.canAccess(tripId, email)) {
            throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found");
        }
        return dayRepository.findByTripIdOrderByDayDateAsc(tripId)
                .stream()
                .map(DayResponse::from)
                .toList();
    }

    public DayResponse create(Long tripId, DayRequest dayRequest, String email) {
        if (!tripRepository.canAccess(tripId, email)) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"); }
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        if (dayRequest.dayDate().isBefore(trip.getStartDate()) || dayRequest.dayDate().isAfter(trip.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Day must be within the trip dates");
        }
        Day day = new Day(trip, dayRequest.dayDate());
        Day saved = dayRepository.save(day);
        return DayResponse.from(saved);
    }

    public void delete(Long dayId, String email) {
        Day day = dayRepository.findById(dayId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"));
        if (!tripRepository.canAccess(day.getTrip().getId(), email)) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"); }
        dayRepository.delete(day);
    }

    public DayResponse update(Long dayId, DayRequest dayRequest, String email) {
        Day day = dayRepository.findById(dayId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"));
        if (!tripRepository.canAccess(day.getTrip().getId(), email)) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"); }
        Trip trip = day.getTrip();
        if (dayRequest.dayDate().isBefore(trip.getStartDate()) || dayRequest.dayDate().isAfter(trip.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Day must be within the trip dates");
        }
        day.setDayDate(dayRequest.dayDate());
        Day saved = dayRepository.save(day);
        return DayResponse.from(saved);
    }

    public DayWeatherResponse getWeather(Long dayId, String email){
        Day day = dayRepository.findById(dayId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"));
        if (!tripRepository.canAccess(day.getTrip().getId(), email)) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"); }
        Place place = day.getPlaces().stream()
                .filter(p -> p.getLatitude() != null)
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No place with coordinates for this day"));

        OpenMeteoResponse response = weatherService.getForecast(place.getLatitude(),place.getLongitude(),day.getDayDate());

        return DayWeatherResponse.from(response);
    }
}
