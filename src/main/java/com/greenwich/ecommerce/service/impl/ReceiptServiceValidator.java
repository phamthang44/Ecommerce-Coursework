package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.util.Util;

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
}
