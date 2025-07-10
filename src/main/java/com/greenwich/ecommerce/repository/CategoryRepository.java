package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByName(String name);
    List<Category> findAllByDeletedFalse();
    Optional<Category> findByName(String categoryName);

    Page<Category> findAllByDeletedFalse(Pageable pageable);

}
