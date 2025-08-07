package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.OrderItemRequestDTO;
import com.greenwich.ecommerce.dto.request.OrderRequestDTO;
import com.greenwich.ecommerce.dto.response.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO createOrderWithSelectedItems(OrderItemRequestDTO items , Long userId);
    OrderResponseDTO createOrderWithAllItems(Long userId);
    OrderResponseDTO getOrderById(String orderId, Long userId);
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO, Long userId);
}
