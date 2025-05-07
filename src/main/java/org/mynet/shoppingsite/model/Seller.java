package org.mynet.shoppingsite.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Seller extends User {
    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String storeName; // 商家的店铺名称

    public Seller() {}

    public Seller(String username, String password){
        super(username, password);
    }

    public Seller(String username, String password, String storeName) {
        super(username, password);
        this.storeName = storeName;
    }

}
