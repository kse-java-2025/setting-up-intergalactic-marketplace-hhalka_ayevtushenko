package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Order;
import com.example.cosmocatsmarket.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "orderId", target = "orderId")
    OrderDTO toDto(Order order);

    @Mapping(target = "orderId", ignore = true)
    Order fromDTO(OrderDTO dto);
    // Order must have cart id
    // @Mapping(source = "cartId", target = "cartId")
    // CartDTO toDto(Cart cart);

    // @Mapping(target = "cartId", ignore = true)
    // Cart fromDTO(CartDTO dto);
}
