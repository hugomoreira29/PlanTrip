package com.plantrip.api.day;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public List<DayResponse> findByTripIdOrderByDayDateAsc(@AuthenticationPrincipal Jwt jwt, @PathVariable Long tripId) {
        return dayService.findAllByTrip(tripId, jwt.getSubject());
    }

    @PostMapping("/trips/{tripId}/days")
    @ResponseStatus(HttpStatus.CREATED)
    public DayResponse create(@AuthenticationPrincipal Jwt jwt, @PathVariable Long tripId, @Valid @RequestBody DayRequest dayRequest) {
        return dayService.create(tripId, dayRequest, jwt.getSubject());
    }

    @DeleteMapping("/days/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        dayService.delete(id, jwt.getSubject());
    }

    @PutMapping("/days/{id}")
    public DayResponse update(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @Valid @RequestBody DayRequest dayRequest) {
        return dayService.update(id, dayRequest, jwt.getSubject());
    }
}
