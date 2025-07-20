package com.greenwich.ecommerce.controller;


import com.greenwich.ecommerce.dto.request.AddressRequestDTO;
import com.greenwich.ecommerce.dto.response.AddressResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.service.AddressService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Address Management", description = "Endpoints for managing address in user account including adding, updating, and setting default address")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<ResponseData<?>> addAddress(@AuthenticationPrincipal SecurityUserDetails user, @RequestBody AddressRequestDTO addressRequestDTO) {
        // Logic to add address
        Long userId = user.getId();
        log.info("Address controller: Adding address for user with ID: {}", userId);
        log.info("Address controller: Address request data: {}", addressRequestDTO.getAddressLine());
        AddressResponseDTO response = addressService.addAddress(userId, addressRequestDTO);
        return ResponseEntity.ok().body(new ResponseData<>(201, "Address added successfully!", response));
    }

    @GetMapping
    public ResponseEntity<?> getAddresses(@AuthenticationPrincipal SecurityUserDetails user) {
        Long userId = user.getId();
        // Logic to get addresses
        return ResponseEntity.ok(addressService.getAddresses(userId));
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ResponseData<AddressResponseDTO>> updateAddress(
            @PathVariable Long addressId,
            @RequestBody AddressRequestDTO addressRequestDTO,
            @AuthenticationPrincipal SecurityUserDetails user) {
        Long userId = user.getId();
        // Logic to update address
        AddressResponseDTO response = addressService.updateAddress(userId, addressId, addressRequestDTO);
        return ResponseEntity.ok().body(new ResponseData<>(200, "Address updated successfully!", response));
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<ResponseData<AddressResponseDTO>> setDefaultAddress(
            @PathVariable Long addressId,
            @AuthenticationPrincipal SecurityUserDetails user) {
        Long userId = user.getId();
        // Logic to set default address
        AddressResponseDTO response = addressService.setDefaultAddress(userId, addressId);
        return ResponseEntity.ok().body(new ResponseData<>(200, "Default address set successfully!", response));
    }

}
