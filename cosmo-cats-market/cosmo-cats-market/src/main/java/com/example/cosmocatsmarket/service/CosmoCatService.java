package com.example.cosmocatsmarket.service;

import com.example.cosmocatsmarket.dto.SpaceCat;
import com.example.cosmocatsmarket.featuretoggle.annotation.FeatureToggle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service
public class CosmoCatService {

    @FeatureToggle("cosmoCats")
    public List<SpaceCat> getCosmoCats() {
        log.info("Fetching CosmoCat list...");

        return List.of(
                new SpaceCat("Nyx", "Void Whisperer"),
                new SpaceCat("Astraeus", "Celestial Cartographer"),
                new SpaceCat("Lunaris", "Moonlit Tracker"),
                new SpaceCat("Solstice", "Solar Gatekeeper")
        );
    }
}
