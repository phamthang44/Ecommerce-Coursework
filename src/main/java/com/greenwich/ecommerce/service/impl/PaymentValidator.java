package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.dto.request.CreatePaymentInput;
import com.greenwich.ecommerce.exception.InvalidDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentValidator {

    public void validatePaymentInputRequest(CreatePaymentInput input, Long userId) {
        if (input == null) {
            log.error("Payment input request is null");
            throw new InvalidDataException("Payment input request cannot be null");
        }

        if (input.getAmount() == null || input.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Invalid payment amount: {}", input.getAmount());
            throw new InvalidDataException("Payment amount must be greater than zero");
        }

        if (input.getOrderId() == null || input.getOrderId() <= 0) {
            log.error("Visa check reference is empty or null");
            throw new InvalidDataException("Visa check reference cannot be empty or null");
        }
        if (userId == null || userId <= 0) {
            log.error("User ID is empty or null");
            throw new InvalidDataException("User ID cannot be empty or null");
        }

    }

}
