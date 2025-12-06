package com.example.cosmocatsmarket.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

@Service
public class PriceClient {

    private final RestTemplate restTemplate = new RestTemplate();
    public double getProductPrice(UUID productId) {
        String url = "http://localhost:8090/prices/" + productId;
        var response = restTemplate.getForEntity(url, Map.class);
        return (Double) response.getBody().get("price");
    }
}
