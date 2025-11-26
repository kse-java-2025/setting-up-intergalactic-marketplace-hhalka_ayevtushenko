package com.example.cosmocatsmarket.mapper;
import com.example.cosmocatsmarket.domain.Order;
import com.example.cosmocatsmarket.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.UUID;

@Mapper(componentModel = "spring", imports = UUID.class)
public interface OrderMapper {

    @Mapping(target = "orderId", expression = "java(order.getOrderId().toString())")
    @Mapping(target = "cartId", expression = "java(order.getCartId().toString())")
    @Mapping(target = "productIds", expression = "java(order.getProductsId().stream().map(UUID::toString).toList())")
    @Mapping(target = "productsCount", source = "productsCount")
    @Mapping(target = "totalPrice", source = "totalPrice")
    OrderDTO toOrderDto(Order order);

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "cartId", expression = "java(UUID.fromString(dto.getCartId()))")
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "productsId", expression = "java(dto.getProductIds().stream().map(UUID::fromString).collect(java.util.stream.Collectors.toSet()))")
    @Mapping(target = "productsCount", source = "productsCount")
    @Mapping(target = "totalPrice", source = "totalPrice")
    Order toOrder(OrderDTO dto);
}
