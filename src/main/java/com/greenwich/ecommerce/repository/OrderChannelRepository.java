package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.OrderChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderChannelRepository extends JpaRepository<OrderChannel, Long> {
}
