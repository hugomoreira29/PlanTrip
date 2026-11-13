package com.plantrip.api.route;

import com.plantrip.api.place.Place;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RouteService {
    private final RestClient restClient;

    public RouteService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://router.project-osrm.org")
                .build();
    }

    public OsrmResponse optimize(List<Place> places){
        String coordinates = places.stream()
                .map(p -> p.getLongitude() + "," + p.getLatitude())
                .collect(Collectors.joining(";"));

        OsrmResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/trip/v1/driving/" + coordinates)
                        .queryParam("source", "first")
                        .queryParam("roundtrip", "false")
                        .queryParam("destination", "any")
                        .queryParam("overview", "false")
                        .build())
                .retrieve()
                .body(OsrmResponse.class);
        return response;
    }
}
