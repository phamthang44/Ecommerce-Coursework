package com.greenwich.ecommerce.controller;


import com.greenwich.ecommerce.dto.request.AddressRequestDTO;
import com.greenwich.ecommerce.service.AddressService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
@Tag(name = "Address Management", description = "Endpoints for managing address in user account")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<?> addAddress(@RequestBody AddressRequestDTO addressRequestDTO) {
        // Logic to add address
        addressService.addAddress(addressRequestDTO);
        return ResponseEntity.ok().body("Address added successfully!");
    }

    @GetMapping
    public ResponseEntity<?> getAddresses(@RequestParam Long userId) {
        // Logic to get addresses
        return ResponseEntity.ok(addressService.getAddresses(userId));
    }

}
