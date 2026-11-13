package com.plantrip.api.trip;

import com.plantrip.api.user.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
    public List<TripResponse> findAll(@AuthenticationPrincipal Jwt jwt) {
        return tripService.findAll(jwt.getSubject());
    }

    @GetMapping("/{id}")
    public TripResponse findById(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return tripService.findById(jwt.getSubject(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TripResponse create(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody TripRequest request) {
        return tripService.create(jwt.getSubject(), request);
    }

    @PutMapping("/{id}")
    public TripResponse update(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @Valid @RequestBody TripRequest request) {
        return tripService.update(jwt.getSubject(), id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        tripService.delete(jwt.getSubject(), id);
    }

    @PostMapping("/{tripId}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse addMember(@AuthenticationPrincipal Jwt jwt, @PathVariable Long tripId, @Valid @RequestBody AddMemberRequest request) {
        return tripService.addMember(jwt.getSubject(), tripId, request);
    }
}
