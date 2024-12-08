package org.mynet.shoppingsite.model;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass // 表示此类不会映射为数据库表
public abstract class User {
    private String username;
    private String password;

    // 无参构造器
    public User() {}

    // 有参构造器
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
