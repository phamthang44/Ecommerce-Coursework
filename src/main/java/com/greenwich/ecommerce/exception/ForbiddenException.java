package com.greenwich.ecommerce.exception;

import com.greenwich.ecommerce.common.enums.ErrorCode;

public class ForbiddenException extends AppException {
    public ForbiddenException(String target) {
        super(ErrorCode.FORBIDDEN, target);
    }
}