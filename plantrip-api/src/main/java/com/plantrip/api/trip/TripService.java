package com.plantrip.api.trip;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TripService {
    private final TripRepository tripRepository;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<TripResponse> findAll() {
        return tripRepository.findAll()
                .stream()
                .map(TripResponse::from)
                .toList();
    }

    public TripResponse findById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        return TripResponse.from(trip);
    }

    public TripResponse create(TripRequest request) {
        if (request.endDate().isBefore(request.startDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        }
        Trip trip = new Trip(request.name(), request.destination(), request.startDate(), request.endDate());
        Trip saved = tripRepository.save(trip);
        return TripResponse.from(saved);
    }
}
