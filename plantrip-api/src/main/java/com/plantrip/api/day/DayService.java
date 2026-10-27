package com.plantrip.api.day;

import com.plantrip.api.trip.Trip;
import com.plantrip.api.trip.TripRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class DayService {
    private final DayRepository dayRepository;
    private final TripRepository tripRepository;

    public DayService(DayRepository dayRepository, TripRepository tripRepository) {
        this.dayRepository = dayRepository;
        this.tripRepository = tripRepository;
    }

    public List<DayResponse> findAllByTrip(Long tripId) {
        if (!tripRepository.existsById(tripId)) {
            throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found");
        }
        return dayRepository.findByTripIdOrderByDayDateAsc(tripId)
                .stream()
                .map(DayResponse::from)
                .toList();
    }

    public DayResponse create(Long tripId, DayRequest dayRequest) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        if (dayRequest.dayDate().isBefore(trip.getStartDate()) || dayRequest.dayDate().isAfter(trip.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Day must be within the trip dates");
        }
        Day day = new Day(trip, dayRequest.dayDate());
        Day saved = dayRepository.save(day);
        return DayResponse.from(saved);
    }

    public void delete(Long dayId) {
        if (!dayRepository.existsById(dayId)) {
            throw new
                    ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found");
        }
        dayRepository.deleteById(dayId);
    }

    public DayResponse update(Long dayId, DayRequest dayRequest) {
        Day day = dayRepository.findById(dayId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Day not found"));
        Trip trip = day.getTrip();
        if (dayRequest.dayDate().isBefore(trip.getStartDate()) || dayRequest.dayDate().isAfter(trip.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Day must be within the trip dates");
        }
        day.setDayDate(dayRequest.dayDate());
        Day saved = dayRepository.save(day);
        return DayResponse.from(saved);
    }
}
