package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.CartItemRequestDTO;
import com.greenwich.ecommerce.dto.response.CartResponseDTO;
import com.greenwich.ecommerce.entity.Cart;

public interface CartService{
//    CartItemRequestDTO addToCart(CartItemRequestDTO cartRequestDTO);
//
//    CartItemRequestDTO removeFromCart(CartItemRequestDTO cartRequestDTO);

//    CartResponseDTO getCartByUserId(Long userId);

    CartResponseDTO getCartByUserId(Long userId);
    CartResponseDTO changeCartItemQuantity(CartItemRequestDTO cartItemRequestDTO, Long userId);
    CartResponseDTO addProductToCart(CartItemRequestDTO cartItemRequestDTO, Long userId);
}
