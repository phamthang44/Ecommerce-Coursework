package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.dto.request.CartItemRequestDTO;
import com.greenwich.ecommerce.dto.response.CartItemResponseDTO;
import com.greenwich.ecommerce.dto.response.CartResponseDTO;
import com.greenwich.ecommerce.entity.Cart;
import com.greenwich.ecommerce.entity.Product;
import com.greenwich.ecommerce.entity.CartItem;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.exception.ResourceNotFoundException;
import com.greenwich.ecommerce.repository.CartITemRepository;
import com.greenwich.ecommerce.repository.CartRepository;
import com.greenwich.ecommerce.repository.ProductRepository;
import com.greenwich.ecommerce.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;


@Slf4j
@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartITemRepository cartItemRepository;

    @Override
    public CartResponseDTO getCartByUserId(Long userId) {
        if (userId == null) {
            log.error("User id is null");
            throw new InvalidDataException("User id is null");
        }

        if (userId <= 0) {
            log.error("User id is negative");
            throw new InvalidParameterException("User id must be greater than zero");
        }

        Cart cart = cartRepository.getByUserId(userId);

        if (cart == null) {
            log.error("Cart not found with id: {}", userId);
            throw new ResourceNotFoundException("Cart not found with id: " + userId);
        }
        return CartResponseDTO.builder()
                .cartItems(cart.getCartItems())

                .totalPrice(cart.getTotalPrice())
                .build();
    }

//    public CartResponseDTO createCart(CartItemRequestDTO cartItemRequestDTO) {
//        Cart cart = Cart.builder()
//                .user(cartItemRequestDTO.getUserId())
//                .cartItems(cartItemRequestDTO.getCartItems())
//                .status("Simulating")
//                .totalPrice(cartItemRequestDTO.getTotalPrice())
//                .build();
//
//        cartRepository.save(cart);
//
//        return CartResponseDTO.builder()
//                .cartItems(cart.getCartItems())
//
//                .totalPrice(cart.getTotalPrice())
//                .build();
//    }

    public CartItemResponseDTO createCartItem(CartItemRequestDTO cartItemRequestDTO) {
        Long productId = cartItemRequestDTO.getProductId();

        Product product = productRepository.getProductById(productId);

        Cart cart = cartRepository.getById(cartItemRequestDTO.getCartId());

        String name = product.getName();
        int quantity = 1;

        CartItem item = new CartItem(cart,product.getCategory(),product,quantity);

        CartItem newItem = cartItemRepository.save(item);

        return CartItemResponseDTO.builder()
                .name(newItem.getProduct().getName())
                .quantity(newItem.getQuantity())
                .build();
    }

//    public CartResponseDTO addToCart(Long cartId, Long productId, int quantity) {
//
//    }
}
