package com.example.cosmocatsmarket.IT;

import com.example.cosmocatsmarket.repository.CartRepository;
import com.example.cosmocatsmarket.repository.entity.CartEntity;
import com.example.cosmocatsmarket.service.CartDbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class CartDbServiceImplIt extends AbstractIt {

    @Autowired
    private CartDbService cartDbService;


    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void cleanDb() {
        cartRepository.deleteAll();
    }

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


    @Test
    @DisplayName("shouldReturnAllCarts: Find all carts")
    void shouldReturnAllCarts() {
        CartEntity c1 = cartDbService.saveCart(
                CartEntity.builder().numProduct(1).totalPrice(5.0).build());
        CartEntity c2 = cartDbService.saveCart(
                CartEntity.builder().numProduct(2).totalPrice(7.5).build());

        var all = cartDbService.findAllCarts();

        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(c -> c.getId().equals(c1.getId())));
        assertTrue(all.stream().anyMatch(c -> c.getId().equals(c2.getId())));
    }


    @Test
    @DisplayName("shouldUpdateCart: Update existing cart")
    void shouldUpdateCart() {
        CartEntity saved = cartDbService.saveCart(
                CartEntity.builder().numProduct(3).totalPrice(10.0).build());

        CartEntity updated = CartEntity.builder()
                .numProduct(10)
                .totalPrice(77.7)
                .build();

        CartEntity result = cartDbService.updateCart(saved.getId(), updated);

        assertEquals(10, result.getNumProduct());
        assertEquals(77.7, result.getTotalPrice());
    }


    @Test
    @DisplayName("shouldDeleteCart: Delete existing cart")
    void shouldDeleteCart() {
        CartEntity saved = cartDbService.saveCart(
                CartEntity.builder().numProduct(5).totalPrice(20.0).build());

        cartDbService.deleteCart(saved.getId());

        Optional<CartEntity> found = cartDbService.findCartById(saved.getId());
        assertTrue(found.isEmpty());
    }


}
