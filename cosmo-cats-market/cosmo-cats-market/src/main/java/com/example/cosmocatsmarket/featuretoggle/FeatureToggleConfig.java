package com.example.cosmocatsmarket.featuretoggle;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Getter
@Configuration
@ConfigurationProperties(prefix = "feature")
public class FeatureToggleConfig {
    private Map<String, FeatureProperties> toggles;
}