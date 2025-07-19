package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Address;
import com.greenwich.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

//    User findByEmail(String mail);
    Optional<Object> findByEmail(String email);

}
