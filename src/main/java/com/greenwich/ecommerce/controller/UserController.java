package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.RegisterRequestDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.dto.response.ResponseError;
import com.greenwich.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getUser(@RequestParam int id) {
//        Cart a = service.getCart(id);
//
//    }



}
