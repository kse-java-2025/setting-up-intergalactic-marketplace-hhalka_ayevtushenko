package com.example.cosmocatsmarket.service;

import com.example.cosmocatsmarket.featuretoggle.exception.FeatureNotAvailableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource(properties = {
        "feature.toggles.cosmoCats.enabled=false"
})
class CosmoCatServiceFeatureDisabledTest {
    @Autowired
    private CosmoCatService cosmoCatService;

    @Test
    @DisplayName("Should return exception when cosmoCats feature is disabled")
    void getCosmoCats_throws_whenFeatureDisabled() {
        assertThrows(FeatureNotAvailableException.class,
                () -> cosmoCatService.getCosmoCats());
    }
}
