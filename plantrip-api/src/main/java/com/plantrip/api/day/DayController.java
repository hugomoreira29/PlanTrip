package com.plantrip.api.day;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class DayController {
    private final DayService dayService;

    public DayController(DayService dayService) {
        this.dayService = dayService;
    }

    @GetMapping("/trips/{tripId}/days")
    public List<DayResponse> findByTripIdOrderByDayDateAsc(@PathVariable Long tripId) {
        return dayService.findAllByTrip(tripId);
    }

    @PostMapping("/trips/{tripId}/days")
    @ResponseStatus(HttpStatus.CREATED)
    public DayResponse create(@PathVariable Long tripId, @Valid @RequestBody DayRequest dayRequest) {
        return dayService.create(tripId, dayRequest);
    }
}
