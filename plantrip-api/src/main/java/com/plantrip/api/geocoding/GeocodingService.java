package com.plantrip.api.geocoding;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeocodingService {
    private final RestClient restClient;
    private long lastRequestTime = 0;


    public GeocodingService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader("User-Agent", "PlanTrip/1.0")
                .build();
    }

    @Cacheable("geocoding")
    public NominatimResult[] search(String query) {
        waitForRateLimit();
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("q", query)
                        .queryParam("format", "json")
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .body(NominatimResult[].class);
    }

    private synchronized void waitForRateLimit() {
        long now = System.currentTimeMillis();
        long elapsed = now - lastRequestTime;              // quanto tempo passou desde a última?
        if (elapsed < 1000) {                       // ainda não passou 1 segundo?
            try {
                Thread.sleep(1000 - elapsed);       // espera o que falta
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        lastRequestTime = System.currentTimeMillis();
    }
}
