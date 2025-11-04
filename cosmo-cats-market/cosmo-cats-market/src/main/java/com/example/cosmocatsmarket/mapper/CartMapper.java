package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Cart;
import com.example.cosmocatsmarket.dto.CartDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "productId", target = "cartId")
    CartDTO toDto(Cart Cart);

    @Mapping(target = "cartId", ignore = true)
    Cart fromDTO(CartDTO dto);
}
