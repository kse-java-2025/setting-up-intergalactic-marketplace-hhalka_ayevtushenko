package com.example.cosmocatsmarket.service.impl;

import com.example.cosmocatsmarket.repository.OrderRepository;
import com.example.cosmocatsmarket.repository.entity.OrderEntity;
import com.example.cosmocatsmarket.service.OrderDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderDbServiceImpl implements OrderDbService {

    private final OrderRepository orderRepository;

    @Override
    public OrderEntity saveOrder(OrderEntity order) {
        if (order.getOrderNumber() == null || order.getOrderNumber().isEmpty()) {
            String orderNumber = generateOrderNumber();
            order.setOrderNumber(orderNumber);
        }
        if (orderRepository.existsByOrderNumber(order.getOrderNumber())) {
            throw new RuntimeException("Order with number '" + order.getOrderNumber() + "' already exists");
        }
        return orderRepository.save(order);
    }

    @Override
    public Optional<OrderEntity> findOrderById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Optional<OrderEntity> findOrderByNaturalId(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }

    @Override
    public List<OrderEntity> findOrdersByCustomer(String email) {
        return orderRepository.findByCustomerEmail(email);
    }

    @Override
    public List<OrderEntity> findOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }



    @Override
    public List<OrderEntity> findAllOrdersSortedByDate() {
        return orderRepository.findAllByOrderByOrderDateDesc();
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public OrderEntity updateOrderStatus(String orderNumber, String status) {
        OrderEntity order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found with number: " + orderNumber));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    @Override
    public List<OrderRepository.OrderSummary> getAllOrderSummaries() {
        return orderRepository.findAllOrderSummaries();
    }


    private String generateOrderNumber() {
        return "ORD-" +
                LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) +
                "-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    public void processOrder(String orderNumber) {
        OrderEntity order = findOrderByNaturalId(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("PROCESSING");
        orderRepository.save(order);
    }

    public boolean orderExistsByNaturalId(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber).isPresent();
    }

    public OrderEntity updateOrderByNaturalId(String orderNumber, OrderEntity updatedOrder) {
        OrderEntity order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderNumber));

        order.setCustomerEmail(updatedOrder.getCustomerEmail());
        order.setStatus(updatedOrder.getStatus());
        order.setTotalPrice(updatedOrder.getTotalPrice());
        order.setNumProduct(updatedOrder.getNumProduct());
        return orderRepository.save(order);
    }

}