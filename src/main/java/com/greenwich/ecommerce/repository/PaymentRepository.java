package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Order;
import com.greenwich.ecommerce.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByVisaCheckReference(String visaCheckReference);
    List<Payment> findByVisaCheckReferenceIn(List<String> visaCheckReferences);

    List<Payment> findByUserId(Long userId);
    Page<Payment> findByUserId(Long userId, Pageable pageable);

    Payment getPaymentByOrder(Order order);
}
