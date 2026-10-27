package com.plantrip.api.day;

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
}
