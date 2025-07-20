package com.greenwich.ecommerce.repository;


import com.greenwich.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product getProductById(Long productId);

    Page<Product> findAllByDeletedFalse(Pageable pageable);
    List<Product> findAllByDeletedFalse();

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> findByNameContainingIgnoreCaseAndDeletedFalse(@Param("keyword") String keyword, Pageable pageable);
}
