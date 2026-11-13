package com.plantrip.api.place;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PlaceController {
    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping("/days/{dayId}/places")
    public List<PlaceResponse> getPlaces(@AuthenticationPrincipal Jwt jwt, @PathVariable Long dayId) {
        return placeService.findAllByDay(dayId, jwt.getSubject());
    }

    @PostMapping("/days/{dayId}/places")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaceResponse create(@AuthenticationPrincipal Jwt jwt, @PathVariable Long dayId, @Valid @RequestBody PlaceRequest placeRequest) {
        return placeService.create(dayId, placeRequest, jwt.getSubject());
    }

    @DeleteMapping("/places/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        placeService.delete(id, jwt.getSubject());
    }

    @PutMapping("/places/{id}")
    public PlaceResponse update(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id, @Valid @RequestBody PlaceRequest placeRequest) {
        return placeService.update(id, placeRequest, jwt.getSubject());
    }
}
