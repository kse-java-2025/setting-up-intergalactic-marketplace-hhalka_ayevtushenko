package com.example.cosmocatsmarket.service;

import com.example.cosmocatsmarket.dto.SpaceCat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "feature.toggles.cosmoCats.enabled=true",
        "feature.toggles.kittyProducts.enabled=false"
})
public class CosmoCatServiceTest {
    @Autowired
    private CosmoCatService cosmoCatService;

    @DisplayName("Should return cosmo cats when feature is enabled")
    @Test
    void getCosmoCats_executes_whenFeatureEnabled() {
        List<SpaceCat> cats = cosmoCatService.getCosmoCats();

        assertThat(cats).isNotEmpty();
        assertThat(cats.toString()).contains("Nyx", "Void Whisperer");
    }
}
