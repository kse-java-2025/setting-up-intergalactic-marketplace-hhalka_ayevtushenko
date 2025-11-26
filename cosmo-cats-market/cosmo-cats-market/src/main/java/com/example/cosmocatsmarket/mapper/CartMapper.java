package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Cart;
import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.CartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Collections;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "productIds", source = "products")
    CartDTO toCartDto(Cart cart);

    @Mapping(target = "products", source = "productIds")
    Cart toCart(CartDTO dto);

    default String map(Product product) {
        return product.getProductId().toString();
    }

    default Product map(String id) {
        return new Product(
                UUID.fromString(id),
                null, null, null, null, null,
                Collections.emptyList(),
                Collections.emptyList()
        );
    }
}
