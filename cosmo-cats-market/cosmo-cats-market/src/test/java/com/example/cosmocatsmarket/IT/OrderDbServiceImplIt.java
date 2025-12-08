package com.example.cosmocatsmarket.IT;

import com.example.cosmocatsmarket.repository.CategoryRepository;
import com.example.cosmocatsmarket.repository.OrderRepository;
import com.example.cosmocatsmarket.repository.entity.CartEntity;
import com.example.cosmocatsmarket.repository.entity.OrderEntity;
import com.example.cosmocatsmarket.service.CartDbService;
import com.example.cosmocatsmarket.service.OrderDbService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class OrderDbServiceImplIt extends AbstractIt {

    @Autowired
    private OrderDbService orderDbService;

    @Autowired
    private CartDbService cartDbService;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void cleanDb() {
        orderRepository.deleteAll();
    }


    @Test
    @DisplayName("shouldCreateAndReadOrderFromDb: Create order and read it by id")
    void shouldCreateAndReadOrderFromDb() {
        CartEntity cart = CartEntity.builder()
                .numProduct(2)
                .totalPrice(50.0)
                .build();
        CartEntity savedCart = cartDbService.saveCart(cart);

        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-1");
        order.setCustomerEmail("space.cat@example.com");
        order.setTotalPrice(10.5);
        order.setNumProduct(2);
        order.setCart(savedCart);

        OrderEntity savedOrder = orderDbService.saveOrder(order);

        assertNotNull(savedOrder.getId());

        Optional<OrderEntity> foundOpt = orderDbService.findOrderById(savedOrder.getId());
        assertTrue(foundOpt.isPresent());
        OrderEntity found = foundOpt.get();

        assertEquals("ORD-1", found.getOrderNumber());
        assertEquals("space.cat@example.com", found.getCustomerEmail());
        assertEquals(10.5, found.getTotalPrice());
        assertEquals(2, found.getNumProduct());

        Optional<OrderEntity> byNaturalId = orderDbService.findOrderByNaturalId("ORD-1");
        assertTrue(byNaturalId.isPresent());
        assertEquals(savedOrder.getId(), byNaturalId.get().getId());
    }


    @Test
    @DisplayName("shouldThrowExceptionWhenOrderNumberAlreadyExists")
    void shouldThrowExceptionWhenOrderNumberAlreadyExists() {
        CartEntity cart = cartDbService.saveCart(
                CartEntity.builder()
                        .numProduct(1)
                        .totalPrice(20.0)
                        .build()
        );

        OrderEntity order1 = new OrderEntity();
        order1.setOrderNumber("ORD-DUPLICATE");
        order1.setCustomerEmail("cat@space.com");
        order1.setCart(cart);
        order1.setNumProduct(1);
        order1.setTotalPrice(10.0);
        order1.setStatus("PENDING");

        orderDbService.saveOrder(order1);
        OrderEntity order2 = new OrderEntity();
        order2.setOrderNumber("ORD-DUPLICATE");
        order2.setCustomerEmail("cat2@space.com");
        order2.setCart(cart);
        order2.setNumProduct(1);
        order2.setTotalPrice(10.0);
        order2.setStatus("PENDING");
        assertThrows(RuntimeException.class, () -> orderDbService.saveOrder(order2));
    }


    @Test
    @DisplayName("shouldFindOrdersByCustomer: find orders by customer email")
    void shouldFindOrdersByCustomer() {
        CartEntity cart = cartDbService.saveCart(
                CartEntity.builder()
                        .numProduct(1)
                        .totalPrice(30.0)
                        .build()
        );

        OrderEntity o1 = new OrderEntity();
        o1.setOrderNumber("ORD-A1");
        o1.setCustomerEmail("cat@space.com");
        o1.setTotalPrice(10.0);
        o1.setNumProduct(1);
        o1.setStatus("PENDING");
        o1.setCart(cart);

        OrderEntity o2 = new OrderEntity();
        o2.setOrderNumber("ORD-A2");
        o2.setCustomerEmail("cat@space.com");
        o2.setTotalPrice(15.0);
        o2.setNumProduct(2);
        o2.setStatus("PENDING");
        o2.setCart(cart);

        orderDbService.saveOrder(o1);
        orderDbService.saveOrder(o2);

        var list = orderDbService.findOrdersByCustomer("cat@space.com");
        assertEquals(2, list.size());
    }


    @Test
    @DisplayName("shouldFindOrdersByStatus: find orders by status")
    void shouldFindOrdersByStatus() {
        CartEntity cart = cartDbService.saveCart(
                CartEntity.builder()
                        .numProduct(1)
                        .totalPrice(40.0)
                        .build()
        );

        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-ST");
        order.setCustomerEmail("cat@space.com");
        order.setTotalPrice(11.0);
        order.setNumProduct(1);
        order.setStatus("PENDING");
        order.setCart(cart);

        orderDbService.saveOrder(order);

        var list = orderDbService.findOrdersByStatus("PENDING");
        assertFalse(list.isEmpty());
    }


    @Test
    @DisplayName("shouldUpdateOrderStatus: update status of existing order")
    void shouldUpdateOrderStatus() {
        CartEntity cart = cartDbService.saveCart(
                CartEntity.builder()
                        .numProduct(1)
                        .totalPrice(40.0)
                        .build()
        );

        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-ST2");
        order.setCustomerEmail("cat@space.com");
        order.setTotalPrice(11.0);
        order.setNumProduct(1);
        order.setStatus("PENDING");
        order.setCart(cart);

        orderDbService.saveOrder(order);
        OrderEntity updated =
                orderDbService.updateOrderStatus("ORD-ST2", "DONE");
        assertEquals("DONE", updated.getStatus());
    }


    @Test
    @DisplayName("shouldReturnSortedOrdersByDate")
    void shouldReturnSortedOrdersByDate() {
        CartEntity cart = cartDbService.saveCart(
                CartEntity.builder()
                        .numProduct(1)
                        .totalPrice(99.0)
                        .build()
        );

        OrderEntity order1 = new OrderEntity();
        order1.setOrderNumber("ORD-S1");
        order1.setCustomerEmail("a@a.com");
        order1.setTotalPrice(11.0);
        order1.setNumProduct(1);
        order1.setStatus("PENDING");
        order1.setCart(cart);

        OrderEntity order2 = new OrderEntity();
        order2.setOrderNumber("ORD-S2");
        order2.setCustomerEmail("a@a.com");
        order2.setTotalPrice(12.0);
        order2.setNumProduct(1);
        order2.setStatus("PENDING");
        order2.setCart(cart);

        orderDbService.saveOrder(order1);
        try {
            Thread.sleep(10);
        } catch (InterruptedException ignored) {}
        orderDbService.saveOrder(order2);
        var list = orderDbService.findAllOrdersSortedByDate();
        assertEquals("ORD-S2", list.get(0).getOrderNumber());
    }


    @Test
    @DisplayName("shouldProcessOrder: change status to PROCESSING")
    void shouldProcessOrder() {
        CartEntity cart = cartDbService.saveCart(
                CartEntity.builder().numProduct(1).totalPrice(10.0).build()
        );

        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-PROC");
        order.setCustomerEmail("cat@space.com");
        order.setTotalPrice(5.0);
        order.setNumProduct(1);
        order.setStatus("NEW");
        order.setCart(cart);
        orderDbService.saveOrder(order);
        orderDbService.processOrder("ORD-PROC");
        var updated = orderDbService.findOrderByNaturalId("ORD-PROC").get();
        assertEquals("PROCESSING", updated.getStatus());
    }


    @Test
    @DisplayName("orderExistsByNaturalId: should return true if order exists")
    void shouldReturnTrueIfOrderExistsByNaturalId() {
        CartEntity cart = cartDbService.saveCart(
                CartEntity.builder().numProduct(1).totalPrice(20.0).build()
        );

        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-EXISTS");
        order.setCustomerEmail("cat@space.com");
        order.setTotalPrice(10.0);
        order.setNumProduct(1);
        order.setCart(cart);

        orderDbService.saveOrder(order);
        boolean exists = orderDbService.orderExistsByNaturalId("ORD-EXISTS");
        assertTrue(exists);
    }



    @Test
    @DisplayName("orderExistsByNaturalId: should return false if order does not exist")
    void shouldReturnFalseIfOrderDoesNotExist() {
        boolean exists = orderDbService.orderExistsByNaturalId("NO-SUCH-ORDER");
        assertFalse(exists);
    }


    @Test
    @DisplayName("updateOrderByNaturalId: should update fields of existing order")
    void shouldUpdateOrderByNaturalId() {
        CartEntity cart = cartDbService.saveCart(
                CartEntity.builder().numProduct(1).totalPrice(50.0).build()
        );

        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-UPD");
        order.setCustomerEmail("old@a.com");
        order.setTotalPrice(10.0);
        order.setNumProduct(1);
        order.setStatus("OLD");
        order.setCart(cart);

        orderDbService.saveOrder(order);
        OrderEntity updatedData = new OrderEntity();
        updatedData.setCustomerEmail("new@a.com");
        updatedData.setTotalPrice(99.0);
        updatedData.setNumProduct(5);
        updatedData.setStatus("NEW");
        updatedData.setCart(cart);

        OrderEntity updated = orderDbService.updateOrderByNaturalId("ORD-UPD", updatedData);
        assertEquals("new@a.com", updated.getCustomerEmail());
        assertEquals(99.0, updated.getTotalPrice());
        assertEquals(5, updated.getNumProduct());
        assertEquals("NEW", updated.getStatus());
    }

    @Test
    @DisplayName("getAllOrderSummaries: should return summary projection list")
    void shouldReturnOrderSummaries() {
        CartEntity cart = cartDbService.saveCart(
                CartEntity.builder().numProduct(1).totalPrice(25.0).build()
        );

        LocalDateTime orderDate = LocalDateTime.of(2024, 1, 1, 12, 0);

        OrderEntity order = new OrderEntity();
        order.setOrderNumber("ORD-SUM");
        order.setCustomerEmail("cat@space.com");
        order.setTotalPrice(12.5);
        order.setNumProduct(1);
        order.setStatus("OK");
        order.setCart(cart);
        order.setOrderDate(orderDate);
        order.setCart(cart);

        orderDbService.saveOrder(order);
        var summaries = orderDbService.getAllOrderSummaries();
        assertFalse(summaries.isEmpty());

        var summary = summaries.get(0);
        assertTrue(summary.getOrderNumber().startsWith("ORD-"));
        assertEquals(12.5, summary.getTotalPrice());

        assertEquals("cat@space.com", summary.getCustomerEmail());
        assertEquals(1, summary.getItemCount());
        assertNotNull(summary.getOrderDate());
        assertEquals(orderDate, summary.getOrderDate());
    }

    @Test
    @DisplayName("findByOrderDateBetween & findByOrderDateAfter: should filter orders by date")
    void shouldFilterOrdersByDate() {
        CartEntity cart = cartDbService.saveCart(
                CartEntity.builder().numProduct(1).totalPrice(10.0).build()
        );

        LocalDateTime now = LocalDateTime.now().withNano(0);
        LocalDateTime twoDaysAgo = now.minusDays(2);
        LocalDateTime yesterday = now.minusDays(1);
        LocalDateTime tomorrow = now.plusDays(1);

        OrderEntity oldOrder = new OrderEntity();
        oldOrder.setOrderNumber("ORD-OLD");
        oldOrder.setCustomerEmail("old@cat.com");
        oldOrder.setTotalPrice(5.0);
        oldOrder.setNumProduct(1);
        oldOrder.setStatus("OK");
        oldOrder.setOrderDate(twoDaysAgo);
        oldOrder.setCart(cart);

        OrderEntity midOrder = new OrderEntity();
        midOrder.setOrderNumber("ORD-MID");
        midOrder.setCustomerEmail("mid@cat.com");
        midOrder.setTotalPrice(15.0);
        midOrder.setNumProduct(2);
        midOrder.setStatus("OK");
        midOrder.setOrderDate(yesterday.plusHours(1));
        midOrder.setCart(cart);

        OrderEntity newOrder = new OrderEntity();
        newOrder.setOrderNumber("ORD-NEW");
        newOrder.setCustomerEmail("new@cat.com");
        newOrder.setTotalPrice(20.0);
        newOrder.setNumProduct(3);
        newOrder.setStatus("OK");
        newOrder.setOrderDate(now);
        newOrder.setCart(cart);

        orderRepository.save(oldOrder);
        orderRepository.save(midOrder);
        orderRepository.save(newOrder);

        List<OrderEntity> between = orderRepository.findByOrderDateBetween(yesterday, tomorrow);
        assertEquals(2, between.size());
        List<String> numbersBetween = between.stream()
                .map(OrderEntity::getOrderNumber)
                .toList();
        assertTrue(numbersBetween.contains("ORD-MID"));
        assertTrue(numbersBetween.contains("ORD-NEW"));
        List<OrderEntity> after = orderRepository.findByOrderDateAfter(yesterday.plusHours(2));
        assertEquals(1, after.size());
        assertEquals("ORD-NEW", after.get(0).getOrderNumber());
    }
}
