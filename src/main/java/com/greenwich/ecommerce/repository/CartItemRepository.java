package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    boolean existsByIdAndCartUserId(Long id, Long userId);
}
