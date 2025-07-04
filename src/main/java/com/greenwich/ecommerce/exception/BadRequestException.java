package com.greenwich.ecommerce.exception;

import com.greenwich.ecommerce.common.enums.ErrorCode;

public class BadRequestException extends AppException{
    public BadRequestException(String what) {
        super(ErrorCode.BAD_REQUEST, what);
    }
}
