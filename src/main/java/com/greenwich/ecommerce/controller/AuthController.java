package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.LoginRequest;
import com.greenwich.ecommerce.dto.request.RegisterRequestDTO;
import com.greenwich.ecommerce.dto.response.LoginResponse;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.dto.response.ResponseError;
import com.greenwich.ecommerce.service.UserService;
import com.greenwich.ecommerce.service.impl.JwtTokenService;
import com.greenwich.ecommerce.service.impl.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<ResponseData<?>> login(@Valid @RequestBody LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        String token = jwtTokenService.generateToken(userDetails);

        LoginResponse loginResponse = new LoginResponse(200, "Login successful", token);

        return ResponseEntity.status(200).body(loginResponse);
//        try {
//
//
//        } catch (BadCredentialsException ex) {
//            log.error("Login failed for email: {}", request.getEmail());
//            return ResponseEntity.status(401).body(new ResponseError(401, "Invalid email or password"));
//        } catch (UsernameNotFoundException ex) {
//            log.error("User not found: {}", request.getEmail());
//            return ResponseEntity.status(401).body(new ResponseError(401, "Invalid email or password"));
//        } catch (Exception ex) {
//            log.error("Unexpected error during login: {}", ex.getMessage());
//            return ResponseEntity.status(500).body(new ResponseError(500, "Internal server error"));
//        }
    }

    @Operation(method= "POST", summary="Add user", description="This API allows you to add a new user")
    @PostMapping(value = "/register")
    public ResponseData<?> register(@Valid @RequestBody RegisterRequestDTO user) {

        log.info("Request add user = {}", user.getEmail());
        long userId = userService.registerUser(user);

        return new ResponseData<>(201, "User registered successfully", userId);

    }
}








//        Authentication auth = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
//        );
//
//        UserDetails userDetails = (UserDetails) auth.getPrincipal();
//
//
//        UserDetailsResponse userResponse = UserDetailsResponse.builder()
//                .email(userDetails.getUsername())
//                .build();
//
//        String token = jwtTokenService.generateToken(userDetails);
//        LoginResponse loginResponse = new LoginResponse(200, "Login successful", userResponse, token);
//        return ResponseEntity.ok(loginResponse);