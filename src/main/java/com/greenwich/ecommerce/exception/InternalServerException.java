package com.greenwich.ecommerce.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String failedToRegisterUser) {
        super("An internal server error occurred. Please try again later.");
    }
}
