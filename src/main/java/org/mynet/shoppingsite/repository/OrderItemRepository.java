package org.mynet.shoppingsite.repository;

import org.mynet.shoppingsite.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderIdIn(List<Long> orderIds);
}
