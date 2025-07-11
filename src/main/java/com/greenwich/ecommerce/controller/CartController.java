package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.CartItemRequestDTO;
import com.greenwich.ecommerce.dto.response.CartResponseDTO;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.service.impl.CartServiceImpl;
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
    private final CartServiceImpl cartService;

    // get the cart of the user
    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<CartResponseDTO>> getCart(@PathVariable Long id) {
        // fetch cart id
        log.info("get cart by id {}", id);

        // Thieu service method
        CartResponseDTO cart = cartService.getCartByUserId(id);

        return ResponseEntity.status(200).body(new ResponseData<>(200, "Cart found!", cart));
    }


    @PostMapping("/items")
    public ResponseEntity<ResponseData<CartResponseDTO>> addToCart(@RequestBody CartItemRequestDTO item,
                                                                   @AuthenticationPrincipal SecurityUserDetails user
                                                                   ) {

        log.info("Adding product id to cart: {}", item.getProductId());
        CartResponseDTO addedProduct = cartService.addProductToCart(item, user.getId());

        return ResponseEntity.status(201).body(new ResponseData<>(201, "Product added to cart successfully!", addedProduct));
    }



}
// show cart voi cart Item
// Can cartItem de lam kh hay lam duoc luon??
//    @PostMapping("/list")
//    public ResponseEntity<ResponseData<ProductResponseDTO>> addToCart(@RequestBody ProductResponseDTO product) {
//
//    }