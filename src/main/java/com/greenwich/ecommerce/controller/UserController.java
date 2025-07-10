package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.UserRequestPatchDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;

import com.greenwich.ecommerce.dto.response.UserDetailsResponse;
import com.greenwich.ecommerce.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/v1/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "User Management", description = "Endpoints for managing users")
public class UserController {


    private final UserService userService;

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseData<UserDetailsResponse>> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserRequestPatchDTO dto) {
        log.info("Updating user with ID: {}", id);
        UserDetailsResponse updatedUser = userService.updateUserRole(id, dto.getUserType());
        return ResponseEntity.ok(new ResponseData<>(200, "User updated successfully", updatedUser));
    }





}
