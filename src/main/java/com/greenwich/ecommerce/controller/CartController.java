package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.CartItemRequestDTO;
import com.greenwich.ecommerce.dto.request.CartItemUpdateRequestDTO;
import com.greenwich.ecommerce.dto.request.CartRequestDeleteItemsDTO;
import com.greenwich.ecommerce.dto.response.CartResponseDTO;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.service.CartService;
import com.greenwich.ecommerce.service.impl.CartServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "cart Management", description = "Endpoints for managing cart, including cart Items")
public class CartController {
    private final CartService cartService;

    // get the cart of the user
    @Operation(method= "GET", summary="Get cart info", description="This API allows you to view cart, just need to call this API will return the cart of the user")
    @GetMapping
    public ResponseEntity<ResponseData<CartResponseDTO>> getCart(@AuthenticationPrincipal SecurityUserDetails user) {

        Long userId = user.getId();

        log.info("get cart by id {}", userId);

        CartResponseDTO cart = cartService.getCartByUserId(userId);

        return ResponseEntity.status(200).body(new ResponseData<>(200, "Cart found!", cart));
    }

    @Operation(method= "POST", summary="Add product to cart (and increase product quantity)", description="This API allows you to a product into your cart (and increase product quantity)")
    @PostMapping("/items")
    public ResponseEntity<ResponseData<CartResponseDTO>> addToCart(@RequestBody CartItemRequestDTO item,
                                                                   @AuthenticationPrincipal SecurityUserDetails user
                                                                   ) {

        log.info("Adding product id to cart: {}", item.getProductId());
        CartResponseDTO addedProduct = cartService.addProductToCart(item, user.getId());

        return ResponseEntity.status(201).body(new ResponseData<>(201, "Product added to cart successfully!", addedProduct));
    }

    @Operation(method= "PUT", summary="Update product quantity in cart", description="This API allows you to change product quantity in cart")
    @PutMapping("/items")
    public ResponseEntity<ResponseData<CartResponseDTO>> updateCart(@RequestBody CartItemUpdateRequestDTO item,
                                                                    @AuthenticationPrincipal SecurityUserDetails user
                                                                   ) {
        log.info("Updating (reduce product quantity) product id in cart: {}", item.getCartItemId());
        CartResponseDTO updatedProduct = cartService.changeCartItemQuantity(item, user.getId());

        return ResponseEntity.status(200).body(new ResponseData<>(200, "The item quantity has changed", updatedProduct));
    }

//    @DeleteMapping("/items/{productId}")
//    @Operation(method= "DELETE", summary="Remove product id from cart (DO NOT USE THIS API)", description="This API allows you to remove a product from your cart (using product ID) but there is a problem that when CartItem has same product ID but different variant, it will remove all of them, so this one should be used carefully (May be need to change later so don't use this API for now)")
//    public ResponseEntity<ResponseData<CartResponseDTO>> removeProductFromCart(@PathVariable Long productId,
//                                                                                @AuthenticationPrincipal SecurityUserDetails user) {
//        log.info("Removing product with id {} from cart for user {}", productId, user.getId());
////        CartResponseDTO updatedCart = cartService.removeProductFromCart(productId, user.getId());
//        return ResponseEntity.status(200).body(new ResponseData<>(200, "Product removed from cart successfully!", null));
//    }

    @DeleteMapping("/items/{cartItemId}")
    @Operation(method= "DELETE", summary="Delete cart item by ID", description="This API allows you to delete a cart item by its ID")
    public ResponseEntity<ResponseData<CartResponseDTO>> deleteCartItemById(@PathVariable Long cartItemId,
                                                                             @AuthenticationPrincipal SecurityUserDetails user) {
        log.info("Deleting cart item with ID {} for user {}", cartItemId, user.getId());
        CartResponseDTO updatedCart = cartService.removeCartItemFromCart(cartItemId, user.getId());

        String message = "Cart item deleted successfully!";

        if (updatedCart.getCartItems().isEmpty()) {
            message = "Your cart is empty.";
        }

        return ResponseEntity.status(200).body(new ResponseData<>(200, message, updatedCart));
    }

    @DeleteMapping
    @Operation(method= "DELETE", summary="Clear cart", description="This API allows you to clear your cart")
    public ResponseEntity<ResponseData<CartResponseDTO>> clearCart(@AuthenticationPrincipal SecurityUserDetails user) {
        log.info("Clearing cart for user {}", user.getId());
        CartResponseDTO clearedCart = cartService.removeAllItemsFromCart(user.getId());

        String message = "Cart cleared successfully!";
        if (clearedCart.getCartItems().isEmpty()) {
            message = "Your cart is empty.";
        }

        return ResponseEntity.status(200).body(new ResponseData<>(200, message, clearedCart));

    }

    @DeleteMapping("/items")
    @Operation(method= "DELETE", summary="Delete list cart items", description="This API allows you to delete a list of cart items by their IDs")
    public ResponseEntity<ResponseData<CartResponseDTO>> deleteCartItem(@RequestBody CartRequestDeleteItemsDTO item,
                                                                        @AuthenticationPrincipal SecurityUserDetails user) {
        log.info("Deleting cart item with product id {} for user {}", item, user.getId());
        CartResponseDTO updatedCart = cartService.removeCartItemsFromCart(item, user.getId());
        String message = "Cart items deleted successfully!";

        if (updatedCart.getCartItems().isEmpty()) {
            message += " Your cart is now empty.";
        }
        return ResponseEntity.status(200).body(new ResponseData<>(200, message, updatedCart));
    }

}