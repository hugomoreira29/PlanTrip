package com.plantrip.api.trip;

import com.plantrip.api.user.User;
import com.plantrip.api.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripService(TripRepository tripRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    public List<TripResponse> findAll(String email) {
        return tripRepository.findByOwnerEmail(email)
                .stream()
                .map(TripResponse::from)
                .toList();
    }

    public TripResponse findById(String email, Long id) {
        Trip trip = tripRepository.findByIdAndOwnerEmail(id, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        return TripResponse.from(trip);
    }

    public TripResponse create(String email, TripRequest request) {
        if (request.endDate().isBefore(request.startDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        }
        User owner = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));
        Trip trip = new Trip(request.name(), request.destination(), request.startDate(), request.endDate(), owner);
        Trip saved = tripRepository.save(trip);
        return TripResponse.from(saved);
    }

    public TripResponse update(Long id, TripRequest request) {
        if (request.endDate().isBefore(request.startDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        }
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));

        trip.setName(request.name());
        trip.setDestination(request.destination());
        trip.setStartDate(request.startDate());
        trip.setEndDate(request.endDate());
        tripRepository.save(trip);
        return TripResponse.from(trip);
    }

    public void delete(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        tripRepository.delete(trip);
    }
}
