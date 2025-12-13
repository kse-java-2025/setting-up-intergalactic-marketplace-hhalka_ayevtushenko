package com.example.cosmocatsmarket.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SecuritySmokeIt.TestSecuredController.class)
@Import({
        SecurityConfig.class,
        ApiKeyAuthenticationFilter.class,
        SecuritySmokeIt.TestJwtDecoderConfig.class
})
@TestPropertySource(properties = {
        "security.api-key=test-key"
})
class SecuritySmokeIt {

    @Autowired
    MockMvc mockMvc;

    @Test
    void withoutAuth_returns401() throws Exception {
        mockMvc.perform(get("/__test/secure"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void withInvalidApiKey_returns401_withCustomError() throws Exception {
        mockMvc.perform(get("/__test/secure")
                        .header("X-API-KEY", "wrong-key"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(containsString("Invalid API key")));
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

    @Controller
    static class TestSecuredController {
        @GetMapping("/test/secure")
        @ResponseBody
        String secure() {
            return "ok";
        }
    }
}
