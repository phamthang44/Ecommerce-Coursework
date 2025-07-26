package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
//    Order findById(Long orderId);/
    Order getOrderById(Long orderId);
}
