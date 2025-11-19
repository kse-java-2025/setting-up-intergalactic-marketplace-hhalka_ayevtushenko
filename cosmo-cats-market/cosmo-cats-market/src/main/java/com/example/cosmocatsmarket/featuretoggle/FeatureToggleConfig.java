package com.example.cosmocatsmarket.featuretoggle;

// import lombok.Getter;
//import lombok.Setter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "feature")
public class FeatureToggleConfig {
    private Map<String, FeatureProperties> toggles;
}