package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Cart;
import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.CartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "productIds", source = "products")
    CartDTO toDto(Cart cart);

    @Mapping(target = "products", source = "productIds")
    Cart fromDTO(CartDTO dto);

    default List<String> mapProductsToIds(List<Product> products) {
        if (products == null) return null;
        return products.stream().map(Product::getProductId).map(UUID::toString).collect(Collectors.toList());
    }

    default List<Product> mapIdsToProducts(List<String> ids) {
        if (ids == null) return null;
        return ids.stream().map(id -> {
            Product p = new Product();
            p.setProductId(UUID.fromString(id));
            return p;
        }).collect(Collectors.toList());
    }
}
