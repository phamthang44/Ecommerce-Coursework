package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.enums.UserStatus;
import com.greenwich.ecommerce.common.enums.UserType;
import com.greenwich.ecommerce.dto.request.RegisterRequestDTO;
import com.greenwich.ecommerce.entity.User;
import com.greenwich.ecommerce.repository.UserRepository;
import com.greenwich.ecommerce.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public long registerUser(RegisterRequestDTO registerRequestDTO) {

        if (registerRequestDTO == null) {
            log.error("Register request is null");
            throw new IllegalArgumentException("Register request cannot be null");
        }

        log.info("Registering user with email: {}", registerRequestDTO.getEmail());
        // Save user to the repository and return user ID

        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            log.error("Email {} is already registered", registerRequestDTO.getEmail());
            throw new IllegalArgumentException("Email is already registered");
        }

        if (registerRequestDTO.getPassword() == null || registerRequestDTO.getPassword().isEmpty()) {
            log.error("Password cannot be null or empty");
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if (registerRequestDTO.getUsername() == null || registerRequestDTO.getUsername().isEmpty()) {
            log.error("Username cannot be null or empty");
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        if (registerRequestDTO.getFullName() == null || registerRequestDTO.getFullName().isEmpty()) {
            log.error("Full name cannot be null or empty");
            throw new IllegalArgumentException("Full name cannot be null or empty");
        }

        if (registerRequestDTO.getPhoneNumber() == null || registerRequestDTO.getPhoneNumber().isEmpty()) {
            log.error("Phone number cannot be null or empty");
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        if (userRepository.existsByUsername(registerRequestDTO.getUsername())) {
            log.error("Username {} is already registered", registerRequestDTO.getUsername());
            throw new IllegalArgumentException("Username is already registered");
        }

        String encodedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());
        User user = User.builder()
                .email(registerRequestDTO.getEmail())
                .password(encodedPassword)
                .gender(null)
                .dateOfBirth(null)
                .username(registerRequestDTO.getUsername())
                .fullName(registerRequestDTO.getFullName())
                .phone(registerRequestDTO.getPhoneNumber())
                .type(UserType.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .addresses(null)
                .build();

        return userRepository.save(user).getId();

    }

}
