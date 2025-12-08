package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Cart;
import com.example.cosmocatsmarket.dto.CartDTO;
import com.example.cosmocatsmarket.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class CartMapperTest {
    private final CartMapper mapper = Mappers.getMapper(CartMapper.class);

    @Test
    @DisplayName("shouldMapCartToDto: Map cart to DTO")
    void shouldMapCartToDto() {
        Cart cart = new Cart();
        UUID cartId = UUID.randomUUID();
        cart.setCartId(cartId);

        Product product1 = new Product();
        UUID productId1 = UUID.fromString("aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb");
        product1.setProductId(productId1);
        Product product2 = new Product();
        UUID productId2 = UUID.fromString("cccccccc-4444-5555-6666-dddddddddddd");
        product2.setProductId(productId2);

        cart.setProducts(List.of(product1, product2));
        cart.setProductsCount(2.0);
        cart.setTotalPrice(42.5);
        CartDTO dto = mapper.toDto(cart);

        assertNotNull(dto);
        assertEquals(cart.getCartId(), dto.getCartId());
        assertNotNull(dto.getProductIds());
        assertEquals(2, dto.getProductIds().size());
        assertEquals(productId1.toString(), dto.getProductIds().get(0));
        assertEquals(productId2.toString(), dto.getProductIds().get(1));
        assertEquals(cart.getProductsCount(), dto.getProductsCount());
        assertEquals(cart.getTotalPrice(), dto.getTotalPrice());
    }

    @Test
    @DisplayName("shouldMapDtoToCart: Map DTO to cart")
    void shouldMapDtoToCart() {
        UUID cartId = UUID.randomUUID();
        String productId1 = "aaaaaaaa-1111-2222-3333-bbbbbbbbbbbb";
        String productId2 = "cccccccc-4444-5555-6666-dddddddddddd";

        CartDTO dto = new CartDTO();
        dto.setCartId(cartId);
        dto.setProductIds(List.of(productId1, productId2));
        dto.setProductsCount(2.0);
        dto.setTotalPrice(42.5);
        Cart cart = mapper.fromDTO(dto);

        assertNotNull(cart);
        assertEquals(dto.getCartId(), cart.getCartId());
        assertNotNull(cart.getProducts());
        assertEquals(2, cart.getProducts().size());
        assertEquals(UUID.fromString(productId1), cart.getProducts().get(0).getProductId());
        assertEquals(UUID.fromString(productId2), cart.getProducts().get(1).getProductId());
        assertNotNull(cart.getProductIds());
        assertEquals(dto.getProductIds(), cart.getProductIds());
        assertEquals(dto.getProductsCount(), cart.getProductsCount());
        assertEquals(dto.getTotalPrice(), cart.getTotalPrice());
    }
}
