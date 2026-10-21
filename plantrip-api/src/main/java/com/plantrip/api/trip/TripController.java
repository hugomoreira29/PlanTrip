package com.plantrip.api.trip;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {
    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping
    public List<TripResponse> findAll() {
        return tripService.findAll();
    }

    @GetMapping("/{id}")
    public TripResponse findById(@PathVariable Long id) {
        return tripService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripResponse create(@Valid @RequestBody TripRequest request) {
        return tripService.create(request);
    }
}
