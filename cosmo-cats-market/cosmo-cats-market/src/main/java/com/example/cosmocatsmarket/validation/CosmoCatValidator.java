package com.example.cosmocatsmarket.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;


public class CosmoCatValidator implements ConstraintValidator<CosmoCatAnnotations, String> {

    private static final Set<String> COSMIC_TERMS = Set.of("star", "galaxy", "comet");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null)
            return false;
        for (String w : COSMIC_TERMS) {
            if (value.toLowerCase().contains(w))
                return true;
        }
        return false;
    }
}
