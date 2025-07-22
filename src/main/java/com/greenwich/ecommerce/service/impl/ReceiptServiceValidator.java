package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.util.Util;
import com.greenwich.ecommerce.dto.request.ReceiptRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReceiptServiceValidator {
    public void validateReceiptId(Long receiptId) {
        Util.isLongNumber(receiptId);

        if (receiptId <= 0) {
            throw new IllegalArgumentException("Invalid receipt ID - must be greater than zero");
        }
    }

    public void validateUserId(Long userId) {
        Util.isLongNumber(userId);
        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID - must be greater than zero");
        }
    }

    public void validateReceiptRequest(ReceiptRequestDTO receiptRequestDTO, Long userId) {
        if (receiptRequestDTO == null) {
            log.error("Receipt request is null");
            throw new IllegalArgumentException("Receipt request is null");
        }

//        if (receiptRequestDTO.getPaymentId() == null) {
//            log.error("Payment ID is null");
//            throw new IllegalArgumentException("Payment ID is null");
//        }

        if (userId == null) {
            log.error("User ID is null");
            throw new IllegalArgumentException("User ID is null");
        } else {
            validateUserId(userId);
        }
    }
}
