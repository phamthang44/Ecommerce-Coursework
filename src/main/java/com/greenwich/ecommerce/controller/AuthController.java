package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.LoginRequest;
import com.greenwich.ecommerce.dto.request.RefreshTokenRequest;
import com.greenwich.ecommerce.dto.request.RegisterRequestDTO;
import com.greenwich.ecommerce.dto.response.LoginResponse;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.infra.security.SecurityUserDetails;
import com.greenwich.ecommerce.service.impl.JwtTokenService;
import com.greenwich.ecommerce.service.impl.RedisServiceImpl;
import com.greenwich.ecommerce.service.impl.UserDetailsServiceImpl;
import com.greenwich.ecommerce.service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserServiceImpl userService;
//    private final UserDetailsServiceImpl userDetailsService;
    private final RedisServiceImpl redisService;
    private final UserDetailsServiceImpl userDetailsServiceImpl;


    @Operation(method= "POST", summary="Login account", description="This API allows you to check login response")
    @PostMapping("/login")
    public ResponseEntity<ResponseData<?>> login(@Valid @RequestBody LoginRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityUserDetails userDetails = (SecurityUserDetails) auth.getPrincipal();

        String token = jwtTokenService.generateAccessToken(userDetails);
        String refreshToken = jwtTokenService.generateRefreshToken(userDetails);

        redisService.setValue(
                "refresh:" + userDetails.getUsername(),
                refreshToken,
                7, TimeUnit.DAYS
        );

        LoginResponse loginResponse = new LoginResponse(token, refreshToken);

        ResponseData<LoginResponse> authResponse = new ResponseData<>(200, "Login successful", loginResponse);

        return ResponseEntity.status(200).body(authResponse);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseData<?>> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtTokenService.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseData<>(401, "Invalid refresh token", null));
        }

        String username = jwtTokenService.getEmailFromJwtToken(refreshToken);
        String storedToken = redisService.getValue("refresh:" + username);

        if (storedToken != null && storedToken.equals(refreshToken)) {
            SecurityUserDetails userDetails = (SecurityUserDetails) userDetailsServiceImpl.loadUserByUsername(username);
            String newAccessToken = jwtTokenService.generateAccessToken(userDetails);
            ResponseData<LoginResponse> responseData = new ResponseData<>(200, "Token refreshed successfully", new LoginResponse(newAccessToken, refreshToken));

            return ResponseEntity.ok(responseData);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseData<>(401, "Invalid refresh token", null));
    }

    @Operation(method= "POST", summary="Add user", description="This API allows you to add a new user")
    @PostMapping(value = "/register")
    public ResponseData<?> register(@Valid @RequestBody RegisterRequestDTO user) {

        log.info("Request add user = {}", user.getEmail());
        long userId = userService.registerUser(user);

        return new ResponseData<>(201, "User registered successfully", userId);

    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseData<Void>> logout(HttpServletRequest request) {
        // Nothing just return the message
        log.info("Request logout, username: {}", request.getUserPrincipal().getName());
        redisService.deleteKey("refresh:" + request.getUserPrincipal().getName());
        return ResponseEntity.status(200)
                .body(new ResponseData<>(200, "Logout successful", null));
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