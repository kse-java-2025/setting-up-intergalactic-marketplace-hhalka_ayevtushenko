package com.example.cosmocatsmarket.security;

import com.example.cosmocatsmarket.repository.ProductRepository;
import com.example.cosmocatsmarket.service.ProductService;
import com.example.cosmocatsmarket.web.ProductController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductController.class)
@Import({
        SecurityConfig.class,
        ApiKeyAuthenticationFilter.class,
        ProductSecurityIt.TestJwtDecoderConfig.class
})
@TestPropertySource(properties = {
        "security.api-key=test-key"
})
class ProductSecurityIt {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @MockBean
    ProductRepository productRepository;

    @Test
    void getAll_withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAll_withBearerJwt_returns200() throws Exception {
        when(productService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/products")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getAll_withValidApiKey_returns200() throws Exception {
        when(productService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/products")
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @TestConfiguration
    static class TestJwtDecoderConfig {
        @Bean
        JwtDecoder jwtDecoder() {
            return token -> Jwt.withTokenValue(token)
                    .header("alg", "none")
                    .claim("sub", "test-user")
                    .claim("scope", "products:read")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .build();
        }
    }
}
