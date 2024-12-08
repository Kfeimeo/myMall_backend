package org.mynet.shoppingsite.repository;

import org.mynet.shoppingsite.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUsername(String username);
    Optional<Customer> findById(Long id);
}
