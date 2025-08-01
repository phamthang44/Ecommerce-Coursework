package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    boolean existsByIdAndOrderUserId(Long id, Long userId);
}
