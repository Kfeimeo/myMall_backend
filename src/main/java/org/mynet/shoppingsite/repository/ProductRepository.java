package org.mynet.shoppingsite.repository;

import org.mynet.shoppingsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 你可以在这里添加一些自定义的查询方法，如果有需求的话
    @Override
    Optional<Product> findById(Long Id);
    @Query("SELECT p FROM Product p WHERE p.seller_id = :sellerId")
    List<Product> findBySellerId(Long sellerId);
}


