package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.RecipientRequestDTO;
import com.greenwich.ecommerce.dto.response.RecipientResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.entity.Recipient;
import com.greenwich.ecommerce.service.RecipientService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/recipients")
public class RecipientController {

    private final RecipientService recipientService;

    @PostMapping
    @Operation(method = "POST", summary = "Add a new recipient", description = "This API allows you to add a new recipient to the system.")
    public ResponseEntity<ResponseData<RecipientResponseDTO>> addRecipient(@RequestBody RecipientRequestDTO dto) {
        log.info("Adding recipient {}", dto.getName());

        RecipientResponseDTO response = recipientService.createRecipient(dto);
        return ResponseEntity.status(201).body(new ResponseData<>(201, "Recipient created successfully", response));
    }

    @GetMapping("/{recipientId}")
    public ResponseEntity<ResponseData<RecipientResponseDTO>> getRecipient(@PathVariable Long recipientId) {
        log.info("Fetching recipient with ID {}", recipientId);

        RecipientResponseDTO response = recipientService.getRecipient(recipientId);
        return ResponseEntity.ok(new ResponseData<>(200, "Recipient found", response));
    }

    @GetMapping
    public ResponseEntity<ResponseData<List<RecipientResponseDTO>>> getAllRecipients() {
        log.info("Fetching all recipients");

        List<RecipientResponseDTO> response = recipientService.getAllRecipients();
        return ResponseEntity.ok(new ResponseData<>(200, "List Recipients found", response));
    }

}
