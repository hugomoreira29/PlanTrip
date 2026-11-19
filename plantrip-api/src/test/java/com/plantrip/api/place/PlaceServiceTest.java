package com.plantrip.api.place;

import com.plantrip.api.day.Day;
import com.plantrip.api.day.DayRepository;
import com.plantrip.api.geocoding.GeocodingService;
import com.plantrip.api.route.OsrmResponse;
import com.plantrip.api.route.OsrmWaypoint;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void optimizeRouteSetsPositionsFromOsrm() {
        User user = new User("test@test.com", "hash");
        Trip trip = new Trip("Lisbon", "Lisbon", LocalDate.of(2026, 11, 18), LocalDate.of(2026, 11, 22), user);
        Day day = new Day(trip, LocalDate.of(2026, 11, 20));

        Place a = new Place(day, "Torre de Belém", null);
        a.setLatitude(new BigDecimal("38.69"));
        a.setLongitude(new BigDecimal("-9.21"));

        Place b = new Place(day, "Castelo de São Jorge", null);
        b.setLatitude(new BigDecimal("38.71"));
        b.setLongitude(new BigDecimal("-9.13"));

        Place c = new Place(day, "Mosteiro dos Jerónimos", null);
        c.setLatitude(new BigDecimal("38.70"));
        c.setLongitude(new BigDecimal("-9.21"));

        day.getPlaces().add(a);
        day.getPlaces().add(b);
        day.getPlaces().add(c);

        when(dayRepository.findById(1L)).thenReturn(Optional.of(day));
        when(tripRepository.canAccess(trip.getId(), "test@test.com")).thenReturn(true);
        when(routeService.optimize(any())).thenReturn(
                new OsrmResponse(List.of(new OsrmWaypoint(0), new OsrmWaypoint(2), new OsrmWaypoint(1))));

        placeService.optimizeRoute(1L, "test@test.com");

        assertEquals(0, a.getPosition());
        assertEquals(2, b.getPosition());
        assertEquals(1, c.getPosition());
    }
}
