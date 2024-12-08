package org.mynet.shoppingsite.repository;

import org.mynet.shoppingsite.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 你可以在这里添加一些自定义的查询方法，如果有需求的话
    Optional<Product> findById(Long Id);
}


