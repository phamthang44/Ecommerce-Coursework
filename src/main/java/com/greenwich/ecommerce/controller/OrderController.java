package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.OrderItemRequestDTO;
import com.greenwich.ecommerce.dto.request.OrderRequestDTO;
import com.greenwich.ecommerce.dto.response.OrderItemResponseDTO;
import com.greenwich.ecommerce.dto.response.OrderResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Order Management", description = "Endpoints for managing orders (save user's order into the database, etc.. (khi nao lam toi thi liet ke sau)")
public class OrderController {

    private final OrderService orderService;

//    @GetMapping("/{orderId}")
//    @Operation(method= "GET", summary="Get order by ID", description="This API allows you to get an order by its ID")
//    public ResponseEntity<ResponseData<OrderResponseDTO>> getOrderById(@PathVariable("orderId") Long orderId) {
//
//        OrderResponseDTO order = orderService.getOrderById(orderId);
//
//        log.info("Fetching order with ID: {}", orderId);
//        return ResponseEntity.ok(new ResponseData<>(200, "Order found!", order));
//    }
//
//    @PostMapping()
//    @Operation(method= "POST", summary="Create order with all cart Items", description="This API allows you to create an order with all items in cart")
//    public ResponseEntity<ResponseData<OrderResponseDTO>> createOrderWithAllItem(@AuthenticationPrincipal SecurityUserDetails user) {
//        log.info("Creating order for user {} with all item in cart", user.getId());
//       OrderResponseDTO madeOrder = orderService.createOrderWithAllItems(user.getId());
//
//        return ResponseEntity.ok(new ResponseData<>(200, "Order created successfully", null));
//    }
//
//    @PostMapping("/items")
//    @Operation(method= "POST", summary="Create order with selected items", description="This API allows you to create an order with selected item's ids in cart")
//    public ResponseEntity<ResponseData<OrderResponseDTO>> createOrderWithSelectedItems(@RequestBody OrderItemRequestDTO orderItem, @AuthenticationPrincipal SecurityUserDetails user ) {
//        log.info("Creating order for user {} with selected item", user.getId());
//       OrderResponseDTO makeOrder = orderService.createOrderWithSelectedItems(orderItem, user.getId());
//
//        return ResponseEntity.ok(new ResponseData<>(200, "Order created successfully", null));
//    }
//--------------------------------------------- Trên là của Thành -----------------------------------

    @PostMapping
    @Operation(method= "POST", summary="Create order with selected items", description="This API allows you to create an order with selected item's ids in cart")
    public ResponseEntity<ResponseData<OrderResponseDTO>> createOrder(@RequestBody OrderRequestDTO request, @AuthenticationPrincipal SecurityUserDetails user) {
        log.info("Creating order item for user {} with selected item", user.getId());
        OrderResponseDTO orderItemResponse = orderService.createOrder(request, user.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseData<>(200, "Order item created successfully", orderItemResponse));
    }
}
