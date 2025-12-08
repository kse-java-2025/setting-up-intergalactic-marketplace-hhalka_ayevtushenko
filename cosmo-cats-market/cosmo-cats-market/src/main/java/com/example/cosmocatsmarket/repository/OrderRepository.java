package com.example.cosmocatsmarket.repository;

import com.example.cosmocatsmarket.repository.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByOrderNumber(String orderNumber);
    Optional<OrderEntity> findByOrderNumber(String orderNumber);
    List<OrderEntity> findByCustomerEmail(String customerEmail);
    List<OrderEntity> findByStatus(String status);
    List<OrderEntity> findByCartId(Long cartId);

    List<OrderEntity> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
    List<OrderEntity> findByOrderDateAfter(LocalDateTime date);
    List<OrderEntity> findAllByOrderByOrderDateDesc();
    List<OrderEntity> findAllByOrderByTotalPriceDesc();

    @Query("SELECT o.orderNumber as orderNumber, o.customerEmail as customerEmail, " +
            "o.totalPrice as totalPrice, o.numProduct as itemCount, o.status as status, " +
            "o.orderDate as orderDate " +
            "FROM OrderEntity o")

    List<OrderSummary> findAllOrderSummaries();

    interface OrderSummary {
        String getOrderNumber();
        String getCustomerEmail();
        Double getTotalPrice();
        Integer getItemCount();
        String getStatus();
        LocalDateTime getOrderDate();
    }

    @Query("SELECT o.status, COUNT(o), SUM(o.totalPrice) " +
            "FROM OrderEntity o " +
            "GROUP BY o.status")

    List<Object[]> getOrderStatsSimple();
}