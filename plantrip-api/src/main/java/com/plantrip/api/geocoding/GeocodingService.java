package com.plantrip.api.geocoding;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeocodingService {
    private final RestClient restClient;
    public GeocodingService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader("User-Agent", "PlanTrip/1.0")
                .build();
    }

    public NominatimResult[] search(String query) {
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("format", "json")
                        .queryParam("limit",1)
                        .build())
                .retrieve()
                .body(NominatimResult[].class);
    }
}
