package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.ReceiptRequestDTO;
import com.greenwich.ecommerce.dto.response.ReceiptResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.entity.User;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.service.ReceiptService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/receipt")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Receipt Management", description = "Endpoints for managing receipts, including get receipt")
public class ReceiptController {
    private final ReceiptService receiptService;

    @PostMapping
    public ResponseData<ReceiptResponseDTO> makeReceipt(@RequestBody ReceiptRequestDTO request, @AuthenticationPrincipal SecurityUserDetails user) {

        log.info("Creating receipt for user {}", user.getId());

        User user1 = user.getUser();

        ReceiptResponseDTO receipt = receiptService.createReceipt(request, user1);

        return new ResponseData<>(200, "Receipt created successfully");
    }
}
