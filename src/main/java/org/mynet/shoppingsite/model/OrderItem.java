package org.mynet.shoppingsite.model;
import jakarta.persistence.*;



@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;
    @Column(name = "order_id",nullable = false)
    Long orderId ;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    public OrderItem(Long productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public OrderItem() {

    }


    // Getters and Setters

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setOrderId(Long order_item_id) {
        this.orderId = order_item_id;
    }

    public Long getOrderId() {
        return orderId;
    }
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

}
