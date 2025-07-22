package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.ReceiptRequestDTO;
import com.greenwich.ecommerce.dto.response.ReceiptResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.entity.User;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.service.ReceiptService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/v1/receipt")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Receipt Management", description = "Endpoints for managing receipts, including get receipt")
public class ReceiptController {
    private final ReceiptService receiptService;

    @PostMapping
    public ResponseData<ReceiptResponseDTO> makeReceipt(@RequestBody ReceiptRequestDTO request, @AuthenticationPrincipal SecurityUserDetails user) {

        Long userId = user.getId();
        log.info("Creating receipt for user {}", userId);

        ReceiptResponseDTO receipt = receiptService.createReceipt(request, userId);

        return new ResponseData<>(200, "Receipt created successfully");
    }

    @GetMapping("/{receiptId}")
    public ResponseData<ReceiptResponseDTO> getReceiptById(@PathVariable Long receiptId, @AuthenticationPrincipal SecurityUserDetails user) {
        Long userId = user.getId();
        log.info("Fetching receipt with ID: {} of user {}", receiptId, userId);

        ReceiptResponseDTO receipt = receiptService.getReceiptById(receiptId, userId);

        return new ResponseData<>(200, "Receipt found", receipt);
    }
}
