package org.mynet.shoppingsite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略
    public Long id;
    public String name;
    public String description;
    public LocalDateTime created_at;
    public LocalDateTime updated_at;
}
