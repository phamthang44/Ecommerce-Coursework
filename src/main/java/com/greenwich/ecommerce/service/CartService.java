package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.CartItemRequestDTO;
import com.greenwich.ecommerce.dto.request.CartItemUpdateRequestDTO;
import com.greenwich.ecommerce.dto.request.CartRequestDeleteItemsDTO;
import com.greenwich.ecommerce.dto.response.CartResponseDTO;
import com.greenwich.ecommerce.entity.Cart;

public interface CartService{
//    CartItemRequestDTO addToCart(CartItemRequestDTO cartRequestDTO);
//
//    CartItemRequestDTO removeFromCart(CartItemRequestDTO cartRequestDTO);

//    CartResponseDTO getCartByUserId(Long userId);

    CartResponseDTO getCartByUserId(Long userId);
    CartResponseDTO changeCartItemQuantity(CartItemUpdateRequestDTO cartItemRequestDTO, Long userId);
    CartResponseDTO addProductToCart(CartItemRequestDTO cartItemRequestDTO, Long userId);
    CartResponseDTO removeCartItemFromCart(Long cartItemId, Long userId);
    CartResponseDTO removeCartItemsFromCart(CartRequestDeleteItemsDTO items, Long userId);
    CartResponseDTO removeAllItemsFromCart(Long userId);
    Cart getCartByUserIdEntity(Long userId);
}
