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
public class Customer extends User {
    // Getters and Setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String address; // 消费者地址

    @Column
    private String email; // 消费者电子邮箱
    public Customer() {}

    public Customer(String username, String password) {
        super(username, password);
    }
    public Customer(CustomerUpdate update){
        super(update.username, update.password);
        this.email = update.email;
        this.id = update.id;
        this.address=update.address;
    }

}

