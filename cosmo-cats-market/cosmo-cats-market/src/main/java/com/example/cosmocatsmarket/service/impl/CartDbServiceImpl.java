package com.example.cosmocatsmarket.service.impl;

import com.example.cosmocatsmarket.repository.CartRepository;
import com.example.cosmocatsmarket.repository.entity.CartEntity;
import com.example.cosmocatsmarket.service.CartDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartDbServiceImpl implements CartDbService {
    private final CartRepository cartRepository;

    @Override
    public CartEntity saveCart(CartEntity cart) {
        return cartRepository.save(cart);
    }

    @Override
    public List<CartEntity> findAllCarts() {
        return cartRepository.findAll();
    }

    @Override
    public Optional<CartEntity> findCartById(Long id) {
        return cartRepository.findById(id);
    }

    @Override
    public void deleteCart(Long id) {
        cartRepository.deleteById(id);
    }

    @Override
    public CartEntity updateCart(Long id, CartEntity cart) {
        CartEntity existingCart = cartRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart not found with id: " + id));

        existingCart.setNumProduct(cart.getNumProduct());
        existingCart.setTotalPrice(cart.getTotalPrice());
        return cartRepository.save(existingCart);
    }

}