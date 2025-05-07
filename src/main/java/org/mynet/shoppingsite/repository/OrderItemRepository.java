package org.mynet.shoppingsite.repository;

import org.mynet.shoppingsite.model.OrderItem;
import org.mynet.shoppingsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderId IN :orderIds")
    ArrayList<OrderItem> findByOrderId(List<Long> orderIds);
    // 原始查询：按sellerId汇总


    // 查询某商户所有商品的累计销量
    @Query("SELECT oi.product, SUM(oi.quantity) " +
            "FROM OrderItem oi " +
            "WHERE oi.product.seller_id = :sellerId " +
            "GROUP BY oi.product.id")

    ArrayList<Object[]> findAccumulatedSalesBySellerId(@Param("sellerId") Long sellerId);



    // 查询某商户在给定时间段内的所有商品累计销量
    @Query("SELECT oi.product, SUM(oi.quantity) " +
            "FROM OrderItem oi " +
            "WHERE oi.product.seller_id = :sellerId " +
            "AND oi.order.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY oi.product.id")
    ArrayList<Object[]> findAccumulatedSalesBySellerIdAndDateRange(@Param("sellerId") Long sellerId,
                                                              @Param("startDate") LocalDateTime startDate,
                                                              @Param("endDate") LocalDateTime endDate);
    // 查询某商户在给定时间段内售出的所有商品
    @Query("SELECT DISTINCT oi.product " +
            "FROM OrderItem oi " +
            "WHERE oi.product.seller_id = :sellerId " +
            "AND oi.order.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY oi.product.id")
    ArrayList<Product> findSoldProductsBySellerIdAndDateRange(@Param("sellerId") Long sellerId,
                                                         @Param("startDate") LocalDateTime startDate,
                                                         @Param("endDate") LocalDateTime endDate);


}
