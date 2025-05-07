package org.mynet.shoppingsite.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "product")
public class Product {
    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String name;
    @Column
    private BigDecimal price;
    @Column
    private String description;
    @Column
    private String image_url; // 商品图片的URL

    @Column
    private Long seller_id; // 卖家ID
    @Column
    private Long stock; // 商品库存数量
    @Column
    private LocalDateTime created_at; // 创建时间
    @Column
    private LocalDateTime updated_at; // 更新时间
    @Column
    private Boolean is_active; // 商品状态（是否可用）

    // 默认构造函数
    public Product() {

    }
}
