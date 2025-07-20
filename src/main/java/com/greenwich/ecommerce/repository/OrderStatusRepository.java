package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.common.enums.OrderStatusType;
import com.greenwich.ecommerce.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {

    @Query("SELECT os FROM OrderStatus os WHERE os.statusName = :orderStatusName")
    OrderStatus findByOrderStatusName(@Param("orderStatusName") OrderStatusType orderStatusName);

}
