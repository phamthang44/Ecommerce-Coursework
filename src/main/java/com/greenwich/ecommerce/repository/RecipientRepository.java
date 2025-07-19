package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecipientRepository extends JpaRepository<Recipient, Long> {
}
