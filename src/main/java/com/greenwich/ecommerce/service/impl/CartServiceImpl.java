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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public CartResponseDTO addProductToCart(CartItemRequestDTO cartItemRequestDTO, Long userId) {

        Long productId = cartItemRequestDTO.getProductId();
        if (productId == null || productId <= 0) {
            log.error("Invalid product id: {}", productId);
            throw new InvalidParameterException("Product id must be greater than zero");
        }

        int quantity = cartItemRequestDTO.getQuantity();
        log.info("Adding product with id {} to cart for user {}", productId, userId);
        Cart cart = cartRepository.getByUserId(userId);
        if (cart == null) {
            log.error("Cart not found for user id: {}", userId);
            //means the user does not have a cart yet, so we create a new one
            cart = createCart(userId);
        }

        Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            existingCartItem.ifPresent(cartItem -> {
                Category category = categoryRepository.findById(3L).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
                cartItem.setProduct(productRepository.getProductById(productId));
                cartItem.setItemType(category);
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                cartItemRepository.save(cartItem);
            });
            log.info("Product with id {} already exists in the cart, updated quantity to {}", productId, quantity);
        }  else {
            Product product = productRepository.getProductById(productId);
            if (product == null) {
                log.error("Product not found with id: {}", productId);
                throw new ResourceNotFoundException("Product not found with id: " + productId);
            }
            Category category = product.getCategory();
            if (category == null) {
                category = categoryRepository.findById(3L).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            }
            CartItem newCartItem = new CartItem(cart, category, product, quantity);
            cart.getCartItems().add(newCartItem);
        }

        Cart savedCart = cartRepository.save(cart);
        CartResponseDTO cartResponseDTO = new CartResponseDTO();
        cartResponseDTO.setCartItems(savedCart.getCartItems().stream()
                .map(cartItem -> CartItemResponseDTO.builder()
                        .name(cartItem.getProduct().getName())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getPrice())
                        .build())
                .toList());
        cartResponseDTO.setTotalPrice(savedCart.getTotalPrice());

        return cartResponseDTO;
    }

    public Cart createCart(Long userId) {
        if (userId == null || userId <= 0) {
            log.error("Invalid user id: {}", userId);
            throw new InvalidParameterException("User id must be greater than zero");
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

//        if (cart == null) {
//            log.error("Cart not found with id: {}", userId);
//            throw new ResourceNotFoundException("Cart not found with id: " + userId);
//        }

        if (cart == null) {
            log.error("Cart not found for user id: {}", userId);
            //means the user does not have a cart yet, so we create a new one
            cart = createCart(userId);
        }

        return CartResponseDTO.builder()
                .cartItems(
                        cart.getCartItems().stream()
                                .map(item -> CartItemResponseDTO.builder()
                                        .productId(item.getProduct().getId())
                                        .name(item.getProduct().getName())
                                        .quantity(item.getQuantity())
                                        .price(item.getProduct().getPrice())
                                        .totalPrice(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))) // logic nay ben FE tu su ly hay sao
                                        .build()
                                )
                                .toList()
                )
                .totalPrice(cart.getTotalPrice())
                .build();

    }
}


//        CartResponseDTO cartResponseDTO = new CartResponseDTO();
//        cart.getCartItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst().ifPresent(item -> {
//            item.setQuantity(item.getQuantity() + quantity);
//            cartItemRepository.save(item);
//
//
//            List<CartItem> cartItems = cart.getCartItems();
//
//            cartResponseDTO.setCartItems(cartItems.stream()
//                    .map(cartItem -> CartItemResponseDTO.builder()
//                            .name(cartItem.getProduct().getName())
//                            .quantity(cartItem.getQuantity())
//                            .price(cartItem.getProduct().getPrice())
//                            .build())
//                    .toList());
//
//
//            cartResponseDTO.setTotalPrice(cart.getTotalPrice());
//        });


//        cart.getCartItems().stream().filter(item -> item.getProduct().getId().equals(productId)).findFirst()
//                .ifPresentOrElse(cartItem -> {
//                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
//                    cartItemRepository.save(cartItem);
//                }, () -> {
//                    Product product = productRepository.getProductById(productId);
//                    if (product == null) {
//                        throw new ResourceNotFoundException("Product not found with id: " + productId);
//                    }
//                    CartItem newCartItem = new CartItem(cart, product.getCategory(), product, quantity);
//                    cartItemRepository.save(newCartItem);
//                    cart.getCartItems().add(newCartItem);
//                });
//        ))
//

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

//    public CartItemResponseDTO createCartItem(CartItemRequestDTO cartItemRequestDTO) {
//        Long productId = cartItemRequestDTO.getProductId();
//
//        Product product = productRepository.getProductById(productId);
//
//        Cart cart = cartRepository.getById(cartItemRequestDTO.getCartId());
//
//        String name = product.getName();
//        int quantity = 1;
//
//        CartItem item = new CartItem(cart,product.getCategory(),product,quantity);
//
//        CartItem newItem = cartItemRepository.save(item);
//
//        return CartItemResponseDTO.builder()
//                .name(newItem.getProduct().getName())
//                .quantity(newItem.getQuantity())
//                .build();
//    }
