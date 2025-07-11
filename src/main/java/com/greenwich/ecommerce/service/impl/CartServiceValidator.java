package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.exception.InvalidDataException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CartServiceValidator {

    public void validateAddToCartRequest(Long userId, Long productId, Integer quantity) {
        validateUserId(userId);
        if (productId == null || productId <= 0) {
            throw new InvalidDataException("Invalid product ID");
        }
        if (quantity == null || quantity <= 0) {
            throw new InvalidDataException("Quantity must be greater than zero");
        }
    }

    public void validateViewCartRequest(Long userId) {
        if (userId == null || userId <= 0) {
            throw new InvalidDataException("Invalid user ID");
        }
    }

    public void validateChangeCartItemQuantityRequest(Long userId, Long productId, Integer quantity) {
        validateUserId(userId);
        if (productId == null || productId <= 0) {
            throw new InvalidDataException("Invalid product ID");
        }
        if (quantity == null || quantity <= 0) {
            throw new InvalidDataException("Quantity must be greater than zero");
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            log.error("Invalid user ID: {}", userId);
            throw new InvalidDataException("Invalid user ID");
        }
    }

}
