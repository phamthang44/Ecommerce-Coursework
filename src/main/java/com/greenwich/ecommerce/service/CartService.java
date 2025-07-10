package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.response.CartResponseDTO;

public interface CartService{
//    CartItemRequestDTO addToCart(CartItemRequestDTO cartRequestDTO);
//
//    CartItemRequestDTO removeFromCart(CartItemRequestDTO cartRequestDTO);

    CartResponseDTO getCartByUserId(Long userId);
}
