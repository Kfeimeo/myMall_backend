package org.mynet.shoppingsite.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;


    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "order_status")
    private Boolean orderStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    // Getters and Setters


}
