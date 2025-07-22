package com.greenwich.ecommerce.exception;

import com.greenwich.ecommerce.common.enums.ErrorCode;

public class PaymentException extends AppException {
    public PaymentException(String target) {
        super(ErrorCode.PAYMENT_ERROR, target);
        System.out.println("PaymentException created with target: " + target +
                ", formattedMessage: " + getFormattedMessage());
    }

    public PaymentException(String target, Throwable cause) {
        super(ErrorCode.PAYMENT_ERROR, target, cause);
        System.out.println("PaymentException created with target: " + target +
                ", cause: " + cause.getMessage() +
                ", formattedMessage: " + getFormattedMessage());
    }
}
