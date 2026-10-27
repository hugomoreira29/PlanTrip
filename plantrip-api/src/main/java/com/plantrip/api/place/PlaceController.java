package com.plantrip.api.place;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public List<PlaceResponse> getPlaces(@PathVariable Long dayId) {
        return placeService.findAllByDay(dayId);
    }

    @PostMapping("/days/{dayId}/places")
    @ResponseStatus(HttpStatus.CREATED)
    public PlaceResponse create(@PathVariable Long dayId, @Valid @RequestBody PlaceRequest placeRequest) {
        return placeService.create(dayId, placeRequest);
    }

    @DeleteMapping("/places/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        placeService.delete(id);
    }

    @PutMapping("/places/{id}")
    public PlaceResponse update(@PathVariable Long id, @Valid @RequestBody PlaceRequest placeRequest) {
        return placeService.update(id, placeRequest);
    }
}
