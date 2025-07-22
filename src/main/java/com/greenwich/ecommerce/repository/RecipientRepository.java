package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Recipient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipientRepository extends JpaRepository<Recipient, Long> {
}
