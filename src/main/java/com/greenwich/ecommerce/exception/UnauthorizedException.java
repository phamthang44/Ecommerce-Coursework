package com.greenwich.ecommerce.exception;

import com.greenwich.ecommerce.common.enums.ErrorCode;

public class UnauthorizedException extends AppException {
    public UnauthorizedException(String target) {
        super(ErrorCode.UNAUTHORIZED, target);
    }
}