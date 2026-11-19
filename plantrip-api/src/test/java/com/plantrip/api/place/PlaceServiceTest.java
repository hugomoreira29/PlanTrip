package com.plantrip.api.place;

import com.plantrip.api.day.Day;
import com.plantrip.api.day.DayRepository;
import com.plantrip.api.geocoding.GeocodingService;
import com.plantrip.api.route.RouteService;
import com.plantrip.api.trip.Trip;
import com.plantrip.api.trip.TripRepository;
import com.plantrip.api.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {
    @Mock
    private PlaceRepository placeRepository;

    @Mock
    private DayRepository dayRepository;
    @Mock
    private GeocodingService geocodingService;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private RouteService routeService;

    @InjectMocks
    private PlaceService placeService;

    @Test
    void optimizeRouteThrowsWhenLessThanTwoPlaces() {
        User user = new User("test@test.com", "hash");
        Trip trip = new Trip("New York", "New York", LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 7), user);
        Day day = new Day(trip, LocalDate.of(2026, 11, 20));
        Place place = new Place(day, "Torre de Belém", null);
        place.setLatitude(new BigDecimal("38.69"));
        place.setLongitude(new BigDecimal("-9.21"));
        day.getPlaces().add(place);

        when(dayRepository.findById(1L)).thenReturn(Optional.of(day));
        when(tripRepository.canAccess(trip.getId(), "test@test.com")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> placeService.optimizeRoute(1L, "test@test.com"));
        verify(routeService, never()).optimize(any());
    }
}
