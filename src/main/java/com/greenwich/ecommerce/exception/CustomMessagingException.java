package com.greenwich.ecommerce.exception;

import com.greenwich.ecommerce.common.enums.ErrorCode;
import jakarta.mail.MessagingException;

public class CustomMessagingException extends AppException {
    public CustomMessagingException(ErrorCode errorCode, String messageArg) {
        super(errorCode, messageArg);
    }
}
