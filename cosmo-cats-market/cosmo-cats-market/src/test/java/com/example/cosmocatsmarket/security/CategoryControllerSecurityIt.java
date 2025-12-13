package com.example.cosmocatsmarket.security;

import com.example.cosmocatsmarket.repository.CategoryRepository;
import com.example.cosmocatsmarket.service.CategoryDbService;
import com.example.cosmocatsmarket.web.CategoryController;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@Import({
        SecurityConfig.class,
        ApiKeyAuthenticationFilter.class,
        CategoryControllerSecurityIt.TestJwtDecoderConfig.class
})
@TestPropertySource(properties = {
        "security.api-key=test-key"
})
class CategoryControllerSecurityIt {

    private static final String BASE = "/api/v1/categories";

    @Autowired MockMvc mockMvc;

    @MockBean CategoryDbService categoryDbService;

    @MockBean CategoryRepository categoryRepository;

    @Test
    void withoutAuth_401() throws Exception {
        mockMvc.perform(get("/test/categories"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void withBearerJwt_200() throws Exception {
        mockMvc.perform(get("/test/categories")
                        .header("Authorization", "Bearer test-token"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void withApiKey_200() throws Exception {
        mockMvc.perform(get("/test/categories")
                        .header("X-API-KEY", "test-key"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @RestController
    @RequestMapping("/test")
    static class TestCategoryController {
        @GetMapping("/categories")
        public String ok() { return "ok"; }
    }

    @TestConfiguration
    static class TestJwtDecoderConfig {
        @Bean JwtDecoder jwtDecoder() {
            return token -> Jwt.withTokenValue(token)
                    .header("alg", "none")
                    .claim("sub", "test-user")
                    .claim("scope", "categories:read")
                    .issuedAt(Instant.now())
                    .expiresAt(Instant.now().plusSeconds(3600))
                    .build();
        }
    }
}
