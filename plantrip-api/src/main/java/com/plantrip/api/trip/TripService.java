package com.plantrip.api.trip;

import com.plantrip.api.user.User;
import com.plantrip.api.user.UserRepository;
import com.plantrip.api.user.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

@Service
public class TripService {
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public TripService(TripRepository tripRepository, UserRepository userRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    public List<TripResponse> findAll(String email) {
        return tripRepository.findAllAccessible(email)
                .stream()
                .map(TripResponse::from)
                .toList();
    }

    public TripResponse findById(String email, Long id) {
        if (!tripRepository.canAccess(id, email)) { throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"); }
        Trip trip = tripRepository.findById(id)
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

    public TripResponse update(String email, Long id, TripRequest request) {
        if (request.endDate().isBefore(request.startDate())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "End date cannot be before start date");
        }
        Trip trip = tripRepository.findByIdAndOwnerEmail(id, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));

        trip.setName(request.name());
        trip.setDestination(request.destination());
        trip.setStartDate(request.startDate());
        trip.setEndDate(request.endDate());
        tripRepository.save(trip);
        return TripResponse.from(trip);
    }

    public void delete(String email, Long id) {
        Trip trip = tripRepository.findByIdAndOwnerEmail(id, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        tripRepository.delete(trip);
    }
    @Transactional
    public UserResponse addMember(String email, Long tripId, AddMemberRequest request) {
        Trip trip = tripRepository.findByIdAndOwnerEmail(tripId, email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip not found"));
        User member = userRepository.findByEmail(request.email().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if(member.getEmail().equals(trip.getOwner().getEmail())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You cannot add yourself in the Trip");
        }

        if (trip.getMembers().contains(member)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User is already a member");
        }

        trip.getMembers().add(member);
        tripRepository.save(trip);
        return UserResponse.from(member);
    }
}
