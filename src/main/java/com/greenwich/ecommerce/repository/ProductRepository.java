package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Category;
import com.greenwich.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product getProductById(Long productId);

    Page<Product> findAllByDeletedFalse(Pageable pageable);
    List<Product> findAllByDeletedFalse();
}
