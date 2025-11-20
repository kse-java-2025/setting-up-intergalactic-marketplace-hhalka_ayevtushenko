package com.example.cosmocatsmarket.featuretoggle.exception;

public class FeatureNotAvailableException extends RuntimeException {

    public FeatureNotAvailableException(String featureName) {
        super("Feature '" + featureName + "' is disabled.");
    }
}
