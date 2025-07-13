package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.dto.request.CartRequestDeleteItemsDTO;
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

    public void validateChangeCartItemQuantityRequest(Long userId, Long cartItemId, Integer quantity) {
        validateUserId(userId);
        if (cartItemId == null || cartItemId <= 0) {
            throw new InvalidDataException("Invalid cart item ID");
        }
        if (quantity == null || quantity < 0) {
            throw new InvalidDataException("Quantity must be greater than zero");
        }
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            log.error("Invalid user ID: {}", userId);
            throw new InvalidDataException("Invalid user ID");
        }
    }

    public void validateDeleteCartItemRequest(Long userId, Long cartItemId) {
        validateUserId(userId);
        if (cartItemId == null || cartItemId <= 0) {
            throw new InvalidDataException("Invalid cart item ID");
        }
    }

    public void validateDeleteCartItemRequest(Long cartItemId) {
        if (cartItemId == null || cartItemId <= 0) {
            throw new InvalidDataException("Invalid cart item ID");
        }
    }

    public void validateDeleteCartItemsRequest(Long userId, CartRequestDeleteItemsDTO cartRequestDeleteItemsDTO) {
        validateUserId(userId);
        if (cartRequestDeleteItemsDTO == null || cartRequestDeleteItemsDTO.getCartItemIds() == null || cartRequestDeleteItemsDTO.getCartItemIds().isEmpty()) {
            throw new InvalidDataException("Invalid request to delete cart items");
        }
        if (cartRequestDeleteItemsDTO.getCartItemIds().stream().anyMatch(id -> id == null || id <= 0)) {
            throw new InvalidDataException("Invalid cart item IDs in the request");
        }

    }


}
