package com.plantrip.api.day;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
