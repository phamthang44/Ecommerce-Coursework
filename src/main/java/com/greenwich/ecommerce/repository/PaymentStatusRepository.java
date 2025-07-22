package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.common.enums.PaymentStatusType;
import com.greenwich.ecommerce.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {

    @Query("SELECT ps FROM PaymentStatus ps WHERE ps.statusName = :paymentStatus")
    PaymentStatus findByPaymentStatus(@Param("paymentStatus") PaymentStatusType paymentStatus);

}
