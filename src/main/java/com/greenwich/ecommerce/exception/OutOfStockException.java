package com.greenwich.ecommerce.exception;

import com.greenwich.ecommerce.common.enums.ErrorCode;

public class OutOfStockException extends AppException {
    public OutOfStockException(String target) {
        super(ErrorCode.OUT_OF_STOCK, target);
        System.out.println("OutOfStockException created with target: " + target +
                ", formattedMessage: " + getFormattedMessage());
    }
}
