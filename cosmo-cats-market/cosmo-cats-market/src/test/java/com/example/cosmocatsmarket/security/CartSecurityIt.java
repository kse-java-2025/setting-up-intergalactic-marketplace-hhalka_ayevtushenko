package com.example.cosmocatsmarket.security;

import com.example.cosmocatsmarket.repository.CategoryRepository;
import com.example.cosmocatsmarket.service.CartDbService;
import com.example.cosmocatsmarket.web.CartController;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CartController.class)
@Import({
        SecurityConfig.class,
        ApiKeyAuthenticationFilter.class,
        CartSecurityIt.TestJwtDecoderConfig.class
})
@TestPropertySource(properties = "security.api-key=test-key")
class CartSecurityIt {

    private static final String BASE = "/api/carts";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartDbService cartDbService;

    @MockBean
    CategoryRepository categoryRepository;

    @RestController
    @RequestMapping("/test")
    static class TestCartController {
        @GetMapping("/carts")
        public String ok() { return "ok"; }
    }

    @Test
    void withBearerJwt_200() throws Exception {
        when(cartDbService.findAllCarts()).thenReturn(List.of());

        mockMvc.perform(get("/api/carts")
                        .header("Authorization", "Bearer test-token"))

                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void withValidApiKey_200() throws Exception {
        mockMvc.perform(get("/api/carts")
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void withoutAuth_401() throws Exception {
        mockMvc.perform(get("/api/carts"))
                .andExpect(status().isUnauthorized());
    }

    @TestConfiguration
    static class TestJwtDecoderConfig {
        @Bean JwtDecoder jwtDecoder() {
            return token -> Jwt.withTokenValue(token)
                    .header("alg", "none")
                    .claim("sub", "test-user")
                    .claim("scope", "cart:read")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .build();
        }
    }
}
