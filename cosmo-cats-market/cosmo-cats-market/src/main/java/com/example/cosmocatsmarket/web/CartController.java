package com.example.cosmocatsmarket.web;

import com.example.cosmocatsmarket.repository.entity.CartEntity;
import com.example.cosmocatsmarket.service.CartDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartDbService cartDbService;

    @GetMapping
    public List<CartEntity> getAllCarts() {
        return cartDbService.findAllCarts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartEntity> getCartById(@PathVariable Long id) {
        return cartDbService.findCartById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CartEntity createCart(@RequestBody CartEntity cart) {
        return cartDbService.saveCart(cart);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CartEntity> updateCart(
            @PathVariable Long id,
            @RequestBody CartEntity cart) {
        try {
            CartEntity updated = cartDbService.updateCart(id, cart);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        cartDbService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}