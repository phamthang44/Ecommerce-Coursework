package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.dto.request.CartItemRequestDTO;
import com.greenwich.ecommerce.dto.response.CartItemResponseDTO;
import com.greenwich.ecommerce.dto.response.CartResponseDTO;
import com.greenwich.ecommerce.entity.*;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.exception.NotFoundException;
import com.greenwich.ecommerce.exception.ResourceNotFoundException;
import com.greenwich.ecommerce.repository.*;
import com.greenwich.ecommerce.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;



@Slf4j
@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CartServiceValidator cartServiceValidator;

    @Override
    @Transactional
    public CartResponseDTO addProductToCart(CartItemRequestDTO cartItemRequestDTO, Long userId) {

        Long productId = cartItemRequestDTO.getProductId();
        int quantity = cartItemRequestDTO.getQuantity();

        cartServiceValidator.validateAddToCartRequest(userId, productId, quantity);
        log.info("Adding product with id {} to cart for user {}", productId, userId);

        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.getProductById(productId);
        if (product == null) {
            log.error("Add to cart : Product not found with id: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        CartItem existingCartItem = findCartItemByProduct(cart, productId);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(quantity); //may be default is quantity = 1 in request.
            log.info("Product with id {} already exists in the cart, updated quantity to {}", productId, quantity);
        }  else {
            CartItem newCartItem = new CartItem(cart, product.getCategory(), product, quantity);
            cart.getCartItems().add(newCartItem);
            log.info("Added new product with id {} to cart", productId);
        }
        return getCartResponseDTO(cartRepository.save(cart));
    }

//    CartResponseDTO.builder()
//            .cartItems(
//            cart.getCartItems().stream()
//                                .map(item -> CartItemResponseDTO.builder()
//            .productId(item.getProduct().getId())
//            .name(item.getProduct().getName())
//            .quantity(item.getQuantity())
//            .price(item.getProduct().getPrice())
//            .subTotalPrice(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
//            .build()
//                                )
//                                        .toList()
//                )
//                        .totalPrice(cart.getTotalPrice())
//            .build();

    private CartResponseDTO getCartResponseDTO(Cart cart) {
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setCartItems(cart.getCartItems().stream()
                .map(cartItem -> CartItemResponseDTO.builder()
                        .productId(cartItem.getProduct().getId())
                        .name(cartItem.getProduct().getName())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getPrice())
                        .subTotalPrice(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())))
                        .build())
                .toList());
        cartResponseDTO.setTotalPrice(cart.getTotalPrice());

        return cartResponseDTO;
    }

    private CartItem findCartItemByProduct(Cart cart, Long productId) {
        return cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }


    private Cart createCart(Long userId) {
        if (userId == null || userId <= 0) {
            log.error("Invalid user id: {}", userId);
            throw new InvalidDataException("User id must be greater than zero");
        }

        Cart cart = new Cart();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new NotFoundException("User not found with id: " + userId);
                });

        cart.setUser(user);
        cart.setStatus("Active");
        return cartRepository.save(cart);
    }

    private Cart getOrCreateCart(Long userId) {
        Cart cart = cartRepository.getByUserId(userId);
        if (cart == null) {
            log.info("No cart found for user id: {}, creating a new one", userId);
            return createCart(userId);
        }
        return cart;
    }

    @Override
    public CartResponseDTO getCartByUserId(Long userId) {

        cartServiceValidator.validateViewCartRequest(userId);
        log.info("Getting cart for user id: {}", userId);

        Cart cart = getOrCreateCart(userId);
        return getCartResponseDTO(cart);

    }

    @Transactional
    @Override
    public CartResponseDTO changeCartItemQuantity(CartItemRequestDTO cartItemRequestDTO, Long userId) {
        Long productId = cartItemRequestDTO.getProductId();
        Integer quantity = cartItemRequestDTO.getQuantity();

        cartServiceValidator.validateChangeCartItemQuantityRequest(userId, productId, quantity);
        log.info("Changing quantity of product {} in cart for user {} to {}", productId, userId, quantity);

        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = findCartItemByProduct(cart, productId);

        Product product = productRepository.getProductById(productId);
        if (product == null) {
            log.error("Change cart item quantity : Product not found with id: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        Category category = product.getCategory();

        if (cartItem == null) {
            CartItem newItem = new CartItem(cart, category, product, quantity);
            cart.getCartItems().add(newItem);
        } else {
            cartItem.setQuantity(quantity);
        }

        return getCartResponseDTO(cartRepository.save(cart));
    }
}
