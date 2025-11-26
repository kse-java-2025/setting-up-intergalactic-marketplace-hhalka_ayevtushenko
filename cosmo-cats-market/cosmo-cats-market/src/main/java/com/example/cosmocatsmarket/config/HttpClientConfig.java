package com.example.cosmocatsmarket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfig {
    private final PriceServiceProperties props;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }
}
