package org.mynet.shoppingsite.repository;

import org.mynet.shoppingsite.model.Order;
import org.mynet.shoppingsite.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    @Query("SELECT SUM(o.totalPrice) FROM Order o")
    BigDecimal calculateTotalSalesAmount();

    @Query("SELECT SUM(o.quantity) FROM OrderItem o")
    Long calculateTotalSalesQuantity();
}

