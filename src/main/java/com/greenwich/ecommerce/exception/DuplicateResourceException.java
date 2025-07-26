package com.greenwich.ecommerce.exception;

import com.greenwich.ecommerce.common.enums.ErrorCode;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }

}
