package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    boolean existsByUserAddress(String userAddress);
}
