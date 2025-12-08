package com.example.cosmocatsmarket.service;

import com.example.cosmocatsmarket.repository.OrderRepository;
import com.example.cosmocatsmarket.repository.entity.OrderEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderDbService {
    OrderEntity saveOrder(OrderEntity order);
    Optional<OrderEntity> findOrderById(Long id);
    Optional<OrderEntity> findOrderByNaturalId(String orderNumber);

    List<OrderEntity> findOrdersByCustomer(String email);
    List<OrderEntity> findOrdersByStatus(String status);
    List<OrderEntity> findAllOrdersSortedByDate();
    void deleteOrder(Long id);
    OrderEntity updateOrderStatus(String orderNumber, String status);
    List<OrderRepository.OrderSummary> getAllOrderSummaries();
    void processOrder(String orderNumber);
    boolean orderExistsByNaturalId(String orderNumber);
    OrderEntity updateOrderByNaturalId(String orderNumber, OrderEntity updatedOrder);
}