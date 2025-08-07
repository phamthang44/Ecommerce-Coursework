package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
//    Order findById(Long orderId);/
    Order getOrderByOrderCode(String orderCode);
}
