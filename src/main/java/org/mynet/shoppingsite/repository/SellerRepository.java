package org.mynet.shoppingsite.repository;

import jakarta.persistence.Table;
import org.mynet.shoppingsite.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Table(schema = "seller")
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByUsername(String username);
}
