package org.mynet.shoppingsite.model;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MappedSuperclass // 表示此类不会映射为数据库表
public abstract class User {
    // Getters and Setters
    private String username;
    private String password;

    // 无参构造器
    public User() {}

    // 有参构造器
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
