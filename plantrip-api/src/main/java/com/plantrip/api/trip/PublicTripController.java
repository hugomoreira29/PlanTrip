package com.plantrip.api.trip;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/trips")
public class PublicTripController {
    private final TripService tripService;
    public PublicTripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/{token}")
    public TripResponse findByShareToken(@PathVariable String token) {
        return tripService.findByShareToken(token);
    }
}
