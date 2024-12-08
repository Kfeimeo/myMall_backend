package org.mynet.shoppingsite.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    private String description;

    private String image_url; // 商品图片的URL

    private Long  category_id; // 商品类别

    private Long stock; // 商品库存数量

    private LocalDateTime created_at; // 创建时间

    private LocalDateTime updated_at; // 更新时间

    private boolean is_active; // 商品状态（是否可用）

    // 默认构造函数
    public Product() {
    }

    // 构造函数
    public Product(String name, BigDecimal price, String description, String image_url, Long category_id, int stock, LocalDateTime created_at, LocalDateTime updated_at, boolean is_active) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image_url = image_url;
        this.category_id = category_id;
        this.stock = (long) stock;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.is_active = is_active;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String imageUrl) {
        this.image_url = imageUrl;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Long category) {
        this.category_id = category;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = (long) stock;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime createdAt) {
        this.created_at = createdAt;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updatedAt) {
        this.updated_at = updatedAt;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean status) {
        this.is_active = status;
    }
}
