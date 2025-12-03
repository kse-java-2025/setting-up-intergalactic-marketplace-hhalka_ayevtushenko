package com.example.cosmocatsmarket.service;

import com.example.cosmocatsmarket.repository.entity.CartEntity;

import java.util.List;
import java.util.Optional;

public interface CartDbService {
    CartEntity saveCart(CartEntity cart);
    Optional<CartEntity> findCartById(Long id);
    List<CartEntity> findAllCarts();
    void deleteCart(Long id);
    CartEntity updateCart(Long id, CartEntity cart);
}