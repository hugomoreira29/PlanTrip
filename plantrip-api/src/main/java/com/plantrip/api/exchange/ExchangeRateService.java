package com.plantrip.api.exchange;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Service
public class ExchangeRateService {
    private final RestClient restClient;

    public ExchangeRateService() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.frankfurter.dev")
                .build();
    }

    @Cacheable("exchange")
    public BigDecimal getRate(String from, String to) {
        FrankfurterResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/latest")
                        .queryParam("base", from)
                        .queryParam("symbols", to)
                        .build())
                .retrieve()
                .body(FrankfurterResponse.class);
        return response.rates().get(to);
    }
}
