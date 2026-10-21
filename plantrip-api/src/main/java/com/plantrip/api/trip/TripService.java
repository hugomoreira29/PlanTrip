package com.plantrip.api.trip;

import org.springframework.stereotype.Service;

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

}
