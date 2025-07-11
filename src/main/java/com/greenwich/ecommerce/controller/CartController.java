package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.CartItemRequestDTO;
import com.greenwich.ecommerce.dto.response.CartResponseDTO;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.service.CartService;
import com.greenwich.ecommerce.service.impl.CartServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
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

//    Change quantity -1
    @Operation(method= "PUT", summary="Reduce product quantity in cart", description="This API allows you to reduce product quantity in cart")
    @PutMapping("/items")
    public ResponseEntity<ResponseData<CartResponseDTO>> updateCart(@RequestBody CartItemRequestDTO item,
                                                                    @AuthenticationPrincipal SecurityUserDetails user
                                                                   ) {
        log.info("Updating (reduce product quantity) product id in cart: {}", item.getProductId());
        CartResponseDTO updatedProduct = cartService.changeCartItemQuantity(item, user.getId());

        return ResponseEntity.status(200).body(new ResponseData<>(200, "Product reduce quantity in cart successfully!", updatedProduct));
    }
}