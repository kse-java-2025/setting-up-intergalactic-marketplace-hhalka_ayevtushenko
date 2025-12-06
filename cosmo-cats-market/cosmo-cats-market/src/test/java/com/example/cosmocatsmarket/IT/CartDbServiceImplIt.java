package com.example.cosmocatsmarket.IT;

import com.example.cosmocatsmarket.repository.entity.CartEntity;
import com.example.cosmocatsmarket.service.CartDbService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CartDbServiceImplIt extends AbstractIt {

    @Autowired
    private CartDbService cartDbService;

    @Test
    @DisplayName("shouldCreateAndReadCartFromDb: Create cart and read it by id")
    void shouldCreateAndReadCartFromDb() {
        CartEntity cart = CartEntity.builder()
                .numProduct(3)
                .totalPrice(10.5)
                .build();

        CartEntity saved = cartDbService.saveCart(cart);

        assertNotNull(saved.getId());

        Optional<CartEntity> foundOpt = cartDbService.findCartById(saved.getId());
        assertTrue(foundOpt.isPresent());
        CartEntity found = foundOpt.get();
        assertEquals(3, found.getNumProduct());
        assertEquals(10.5, found.getTotalPrice());
    }
}
