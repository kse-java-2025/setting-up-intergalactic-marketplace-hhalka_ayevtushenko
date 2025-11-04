package com.example.cosmocatsmarket.mapper;

import com.example.cosmocatsmarket.domain.Order;
import com.example.cosmocatsmarket.domain.Product;
import com.example.cosmocatsmarket.dto.OrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderId", source = "orderId")
    OrderDTO toDto(Order order);

    @Mapping(target = "orderId", ignore = true)
    Order fromDTO(OrderDTO dto);

    default List<String> mapProductsToStrings(List<Product> products) {
        return products == null ? null : products.stream().map(p -> p.getProductId() != null ? p.getProductId().toString() : null).collect(Collectors.toList());
    }


    default List<Product> mapStringsToProducts(List<String> ids) {
        return ids == null ? null : ids.stream().map(id -> {
            Product p = new Product();
            p.setProductId(UUID.fromString(id));
            return p;
        }).collect(Collectors.toList());
    }
}
