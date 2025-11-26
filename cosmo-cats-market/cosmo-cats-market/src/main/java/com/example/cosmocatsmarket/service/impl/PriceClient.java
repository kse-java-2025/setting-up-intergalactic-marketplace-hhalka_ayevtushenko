package com.example.cosmocatsmarket.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.UUID;

@Service
public class PriceClient {

    private final RestClient restClient;

    public PriceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public Double getPrice(UUID productId) {
        return restClient.get()
                .uri("/prices/{id}", productId)
                .retrieve()
                .body(Double.class);
    }
}
