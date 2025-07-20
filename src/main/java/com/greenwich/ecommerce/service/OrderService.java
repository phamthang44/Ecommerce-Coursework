package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.OrderItemRequestDTO;
import com.greenwich.ecommerce.dto.response.OrderResponseDTO;

public interface OrderService {
    public OrderResponseDTO createOrderWithSelectedItems(OrderItemRequestDTO items , Long userId);
    public OrderResponseDTO createOrderWithAllItems(Long userId);
    public OrderResponseDTO getOrderById(Long orderId);
}
