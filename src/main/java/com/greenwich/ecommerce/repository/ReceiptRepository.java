package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Payment;
import com.greenwich.ecommerce.entity.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository <Receipt, Long> {
    Receipt getReceiptById(Long receiptId);

    Page<Receipt> findAllByDeletedFalseAndUserId(Pageable pageable, Long userId);

    @Query("SELECT r FROM Receipt r " +
            "JOIN FETCH r.payment p " +
            "JOIN FETCH p.order o " +
            "WHERE o.orderCode = :orderCode")
    Receipt getReceiptByOrderCode(@Param("orderCode") String orderCode);

    Receipt getReceiptByPayment(Payment payment);
}
