package com.example.cosmocatsmarket.web;

import com.example.cosmocatsmarket.dto.OrderDTO;
import com.example.cosmocatsmarket.repository.entity.CartEntity;
import com.example.cosmocatsmarket.repository.entity.OrderEntity;
import com.example.cosmocatsmarket.service.CartDbService;
import com.example.cosmocatsmarket.service.OrderDbService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderDbService orderDbService;
    private final CartDbService cartDbService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.info("GET /api/v1/orders - Get all orders");
        List<OrderEntity> entities = orderDbService.findAllOrdersSortedByDate();
        List<OrderDTO> dtos = entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable String orderId) {
        log.info("GET /api/v1/orders/{} - Get order by ID", orderId);
        Long id = convertStringToLong(orderId);
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        Optional<OrderEntity> order = orderDbService.findOrderById(id);
        return order.map(entity -> ResponseEntity.ok(convertToDTO(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-number/{orderNumber}")
    public ResponseEntity<OrderDTO> getOrderByNumber(@PathVariable String orderNumber) {
        log.info("GET /api/v1/orders/by-number/{} - Get order by number", orderNumber);
        Optional<OrderEntity> order = orderDbService.findOrderByNaturalId(orderNumber);
        return order.map(entity -> ResponseEntity.ok(convertToDTO(entity)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        log.info("POST /api/v1/orders - Create new order");
        OrderEntity entity = convertToEntity(orderDTO);

        if (entity.getOrderNumber() == null || entity.getOrderNumber().isEmpty()) {
            String orderNumber = "ORD-" + System.currentTimeMillis();
            entity.setOrderNumber(orderNumber);
        }
        if (entity.getOrderDate() == null) {
            entity.setOrderDate(LocalDateTime.now());
        }
        if (entity.getStatus() == null || entity.getStatus().isEmpty()) {
            entity.setStatus("PENDING");
        }
        OrderEntity saved = orderDbService.saveOrder(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDTO(saved));
    }


    @PatchMapping("/{orderNumber}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable String orderNumber,
            @RequestParam String status) {
        log.info("PATCH /api/v1/orders/{}/status - Update order status to {}", orderNumber, status);
        try {
            OrderEntity updated = orderDbService.updateOrderStatus(orderNumber, status);
            return ResponseEntity.ok(convertToDTO(updated));
        } catch (Exception e) {
            log.error("Error updating order status: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }


    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable String orderId) {
        log.info("DELETE /api/v1/orders/{} - Delete order", orderId);
        Long id = convertStringToLong(orderId);
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            orderDbService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting order: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/customer/{email}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable String email) {
        log.info("GET /api/v1/orders/customer/{} - Get orders by customer", email);
        List<OrderEntity> entities = orderDbService.findOrdersByCustomer(email);
        List<OrderDTO> dtos = entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable String status) {
        log.info("GET /api/v1/orders/status/{} - Get orders by status", status);
        List<OrderEntity> entities = orderDbService.findOrdersByStatus(status);
        List<OrderDTO> dtos = entities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private OrderDTO convertToDTO(OrderEntity entity) {
        return OrderDTO.builder()
                .orderId(entity.getId() != null ? entity.getId().toString() : null)
                .cartId(entity.getCart() != null ? entity.getCart().getId().toString() : null)
                .productsCount(entity.getNumProduct())
                .totalPrice(entity.getTotalPrice() != null ? entity.getTotalPrice().intValue() : 0)
                .products(List.of())
                .productIds(List.of())
                .build();
    }


    private OrderEntity convertToEntity(OrderDTO dto) {
        OrderEntity.OrderEntityBuilder builder = OrderEntity.builder();
        if (dto.getOrderId() != null) {
            Long id = convertStringToLong(dto.getOrderId());
            if (id != null) {
                builder.id(id);
            }
        }
        if (dto.getCartId() != null) {
            Long cartId = convertStringToLong(dto.getCartId());
            CartEntity cart = cartDbService.findCartById(cartId)
                    .orElseThrow(() -> new RuntimeException("Cart not found with id " + cartId));
            builder.cart(cart);
        }
        else {
            throw new RuntimeException("cartId is required");
        }
        Double totalPrice = dto.getTotalPrice() != null
                ? dto.getTotalPrice().doubleValue()
                : null;

        String orderNumber = "ORD-" + System.currentTimeMillis();
        return builder
                .orderNumber(orderNumber)
                .numProduct(dto.getProductsCount())
                .totalPrice(totalPrice)
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .build();
    }


    private Long convertStringToLong(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}