package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.ReceiptRequestDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ReceiptResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.entity.User;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/v1/receipts")
@Slf4j
@RequiredArgsConstructor
@Validated
@Tag(name = "Receipt Management", description = "Endpoints for managing receipts, including get receipt")
public class ReceiptController {

    private final ReceiptService receiptService;

    @PostMapping
    @Operation(summary = "Create a new receipt", description = "Creates a new receipt for the authenticated user. (TESTING ONLY)")
    public ResponseEntity<ResponseData<ReceiptResponseDTO>> makeReceipt(@Valid @RequestBody ReceiptRequestDTO request, @AuthenticationPrincipal SecurityUserDetails user) {

        Long userId = user.getId();
        log.info("Creating receipt for user {}", userId);

        ReceiptResponseDTO receipt = receiptService.createReceipt(request, userId);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseData<>(201, "Receipt created successfully", receipt));
    }

    @GetMapping("/{orderCode}")
    @Operation(summary = "Get receipt", description = "View a receipt by its Order code.")
    public ResponseData<ReceiptResponseDTO> getReceiptById(@PathVariable String orderCode, @AuthenticationPrincipal SecurityUserDetails user) {
        Long userId = user.getId();
        log.info("Fetching receipt with ID: {} of user {}", orderCode, userId);

        ReceiptResponseDTO receipt = receiptService.getReceiptByOrderCode(orderCode);

        return new ResponseData<>(200, "Receipt found", receipt);
    }

    @GetMapping
    @Operation(summary = "Get all receipts with pagination", description = "View all receipts of the authenticated user with pagination.")
    public ResponseEntity<ResponseData<PageResponse<ReceiptResponseDTO>>> getAllReceipts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal SecurityUserDetails user) {

        Long userId = user.getId();
        log.info("Fetching all receipts for user {}", userId);

        PageResponse<ReceiptResponseDTO> receipts = receiptService.getAllReceiptsByUserId(userId, page, size);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseData<>(HttpStatus.OK.value(), "List receipts found!", receipts));
    }
}
