package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.util.Util;
import com.greenwich.ecommerce.dto.request.OrderItemRequestDTO;
import com.greenwich.ecommerce.dto.request.OrderRequestDTO;

import com.greenwich.ecommerce.entity.OrderItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderServiceValidator {
    public void validateUserId(Long userId) {
        Util.isLongNumber(userId);

        if (userId <= 0) {
            log.error("Invalid user ID: {}", userId);
            throw new IllegalArgumentException("Invalid user ID - must be greater than zero");
        }
    }

    public void validateOrderId(Long orderId) {
        Util.isLongNumber(orderId);

        if (orderId <= 0) {
            log.error("Invalid order ID: {}", orderId);
            throw new IllegalArgumentException("Invalid order ID - must be greater than zero");
        }
    }

//    public void validateOrderItemRequest(Long userId, OrderItemRequestDTO orderItemRequestDTO) {
//        validateUserId(userId);
//
//        if (orderItemRequestDTO == null || orderItemRequestDTO.getCartItemIds().isEmpty()) {
//            log.error("Invalid order request - items list is empty");
//            throw new IllegalArgumentException("Invalid order request - items list is empty");
//        }
//
//        if (orderItemRequestDTO.getCartItemIds().stream().anyMatch(id -> id == null || id <= 0)) {
//            log.error("Invalid order request - items list contains invalid IDs");
//            throw new IllegalArgumentException("Invalid order request - items list contains invalid IDs");
//        }
//    }
}
