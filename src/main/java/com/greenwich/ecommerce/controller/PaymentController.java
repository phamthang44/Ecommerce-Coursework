package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.CreatePaymentInput;
import com.greenwich.ecommerce.dto.request.PaymentProcessRequest;
import com.greenwich.ecommerce.dto.response.*;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Payment Management", description = "Endpoints for managing payments (process payment, etc.)")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @Operation(summary = "Create Payment", description = "Creates a new payment with the provided details. The payment is created in a pending state. This API will use some information from the order from Checkout Page to create the payment.(Order ID, Amount means total price of the order (which is applied the discount if any))")
    public ResponseEntity<ResponseData<PaymentCreateResponse>> createPayment(@RequestBody CreatePaymentInput input,
                                                                             @AuthenticationPrincipal SecurityUserDetails user) {
        log.info("Payment Controller : Creating payment with details: {}", input);
        PaymentCreateResponse response = paymentService.createPayment(input, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseData<>(201, "Payment created successfully", response));
    }

    @PostMapping("/process")
    @Operation(summary = "Process Payment", description = "Simulate the processes a payment with the provided details. (90% SUCCESS RATE) / (10% FAILURE RATE) ")
    public ResponseEntity<ResponseData<PaymentProcessResponse>> processPayment(@RequestBody PaymentProcessRequest request,
                                                                               @AuthenticationPrincipal SecurityUserDetails user) {
        log.info("Payment Controller : Processing payment with details: {}", request);

        PaymentProcessResponse response = paymentService.processPayment(request, user.getId());

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseData<>(200, response.getMessage(), response));
    }

    @GetMapping
    @Operation(summary = "Get Payment History", description = "Fetches the payment history for the authenticated user with pagination support.")
    public ResponseEntity<ResponseData<PageResponse<PaymentResponse>>> getPaymentHistory(
            @AuthenticationPrincipal SecurityUserDetails user,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        log.info("Payment Controller : Fetching payments history with current user: {}", user.getId());
        PageResponse<PaymentResponse> payments = paymentService.getPaymentsWithPage(pageNo, pageSize, user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseData<>(200, "Payments history", payments));
    }

}
