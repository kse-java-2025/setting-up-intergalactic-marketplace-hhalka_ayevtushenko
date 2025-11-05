package com.example.cosmocatsmarket.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class CosmoCatValidatorTest {
    private CosmoCatValidator validator;
    private ConstraintValidatorContext context;

    @BeforeEach
    void setup() {
        validator = new CosmoCatValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    void shouldReturnTrueWhenNameContainsCosmicTerm() {
        assertTrue(validator.isValid("Star Helmet", context));
        assertTrue(validator.isValid("Galaxy Bowl", context));
        assertTrue(validator.isValid("Comet Dust", context));
    }

    @Test
    void shouldReturnFalseWhenNameDoesNotContainCosmicTerm() {
        assertFalse(validator.isValid("Lemon Helmet", context));
        assertFalse(validator.isValid("Cat Food", context));
        assertFalse(validator.isValid("Apple", context));
    }




}

