package com.example.cosmocatsmarket.featuretoggle;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeatureToggleService {

    private final FeatureToggleConfig featureToggleConfig;
    public boolean isEnabled(String featureName) {
        var feature = featureToggleConfig.getToggles().get(featureName);
        return feature != null && feature.isEnabled();
    }
}