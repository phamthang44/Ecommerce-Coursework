package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository <Receipt, Long> {
    Receipt getReceiptById(Long receiptId);
}
