package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart getByUserId(Long userId);
    Optional<Cart> findByUserId(Long userId);
}
