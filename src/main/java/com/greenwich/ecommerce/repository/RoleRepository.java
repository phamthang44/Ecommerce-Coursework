package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.common.enums.UserType;
import com.greenwich.ecommerce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

}
