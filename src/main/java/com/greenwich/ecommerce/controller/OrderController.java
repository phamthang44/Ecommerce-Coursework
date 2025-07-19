package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.OrderItemRequestDTO;
import com.greenwich.ecommerce.dto.response.OrderItemResponseDTO;
import com.greenwich.ecommerce.dto.response.OrderResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.service.impl.OrderServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Endpoints for managing orders (save user's order into the database, etc.. (khi nao lam toi thi liet ke sau)")
public class OrderController {
    private final OrderServiceImpl orderService;

    @GetMapping("/{orderId}")
    public ResponseEntity<ResponseData<OrderResponseDTO>> getOrderById(@PathVariable("orderId") Long orderId) {

        if (orderId == null) {
            log.error("User ID is null");
        } else if (orderId < 0) {
            log.error("User ID is negative");
        }

        OrderResponseDTO order = orderService.getOrderById(orderId);
        log.info("Fetching order with ID: {}", orderId);
        return ResponseEntity.ok(new ResponseData<>(200, "Order found!", order));
    }

    @PostMapping("/items")
    public ResponseEntity<ResponseData<OrderResponseDTO>> createOrder( @AuthenticationPrincipal SecurityUserDetails user) {
        OrderResponseDTO madeOrder = orderService.makeOrder(user.getId());

        return ResponseEntity.ok(new ResponseData<>(200, "Order created successfully", madeOrder));
    }
}
