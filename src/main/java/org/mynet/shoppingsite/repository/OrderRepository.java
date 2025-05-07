package org.mynet.shoppingsite.repository;

import org.mynet.shoppingsite.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);


    @Query("SELECT SUM(totalPrice) FROM Order")
    Double calculateTotalSalesAmount();

    // 自定义查询，按日期范围查找订单
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);
    @Query("SELECT o FROM Order o JOIN OrderItem oi ON o.orderId = oi.order.orderId JOIN Product p ON oi.product.id = p.id WHERE p.seller_id = :seller_id")
    List<Order> findBySellerId(Long seller_id);
}

