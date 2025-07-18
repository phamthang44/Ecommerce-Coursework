package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.enums.ErrorCode;
import com.greenwich.ecommerce.common.enums.Gender;
import com.greenwich.ecommerce.common.enums.UserStatus;
import com.greenwich.ecommerce.common.enums.UserType;
import com.greenwich.ecommerce.common.mapper.UserMapper;
import com.greenwich.ecommerce.dto.request.RegisterRequestDTO;

import com.greenwich.ecommerce.dto.response.UserDetailsResponse;
import com.greenwich.ecommerce.entity.Role;
import com.greenwich.ecommerce.entity.User;
import com.greenwich.ecommerce.exception.*;
import com.greenwich.ecommerce.infra.email.EmailService;
import com.greenwich.ecommerce.repository.RoleRepository;
import com.greenwich.ecommerce.repository.UserRepository;
import com.greenwich.ecommerce.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final UserMapper userMapper;
    private final EmailService emailService;

    @Transactional
    @Override
    public long registerUser(RegisterRequestDTO registerRequestDTO) {

        if (registerRequestDTO == null) {
            log.error("Register request is null");
            throw new BadRequestException("Register request cannot be null");
        }

        log.info("Registering user with email: {}", registerRequestDTO.getEmail());
        // Save user to the repository and return user ID

        String email = registerRequestDTO.getEmail().trim();
        String username = registerRequestDTO.getUsername().trim();
        String password = registerRequestDTO.getPassword().trim();
        String fullName = registerRequestDTO.getFullName().trim();
        String phoneNumber = registerRequestDTO.getPhoneNumber().trim();

        userValidator.validateEmail(email);
        if (userRepository.existsByEmail(email)) {
            log.warn("Email already exists");
            throw new DuplicateResourceException("Email is already registered");
        }

        userValidator.validatePassword(password);

        userValidator.validateUsername(username);
        if (userRepository.existsByUsername(username)) {
            log.warn("Username {} is already registered", username);
            throw new DuplicateResourceException("Username is already registered");
        }

        userValidator.validateFullName(fullName);

        userValidator.validatePhoneNumber(phoneNumber);

        Gender gender = registerRequestDTO.getGender() != null ? registerRequestDTO.getGender() : Gender.OTHER;
        LocalDate dateOfBirth = registerRequestDTO.getDateOfBirth();


        Role role = roleRepository.findByName(UserType.CUSTOMER.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        String encodedPassword = passwordEncoder.encode(registerRequestDTO.getPassword());

        User user = userMapper.toUser(registerRequestDTO);
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);
        user.saveAddress(null);
        user.setGender(gender);

        log.info("Phone number: {}", user.getPhone());
        user.setPhone(phoneNumber);
        log.info("Phone number: {}", user.getPhone());
        user.setDateOfBirth(dateOfBirth);
        Long userId = userRepository.save(user).getId();
        if (userId != null) {
            log.info("User registered successfully with ID: {}", userId);
            try {
                emailService.sendEmailRegistrationHtml(user.getEmail(), user.getUsername());
            } catch (MessagingException e) {
                log.error("Failed to send registration email to {}", user.getEmail(), e);
                throw new CustomMessagingException(ErrorCode.INTERNAL_ERROR, "Failed to send registration email");
            }
        } else {
            log.error("Failed to register user");
            throw new InternalServerException("Failed to register user");
        }
        return userId;
    }

    public UserDetailsResponse getUserDetailsByEmail(String email) {
        log.info("Fetching user details for email: {}", email);

        User user = (User) userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("User"));

        return UserDetailsResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhone())
                .role(user.getRole().getName())
                .build();

    }

    @Override
    public UserDetailsResponse updateUserRole(Long userId, UserType userType) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User"));

        Role newRole = roleRepository.findById(2L) //hardcoded
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        user.setRole(newRole);
        userRepository.save(user);

        return UserDetailsResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhone())
                .role(newRole.getName())
                .build();
    }

    @Override
    public UserDetailsResponse getCurrentUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        return UserDetailsResponse.builder()
                .id(user.getId())
                .address(user.getAddresses().toString())
                .email(user.getEmail())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhone())
                .role(user.getRole().getName())
                .build();
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with id: {}", userId);
                    return new NotFoundException("User not found with id: " + userId);
                });
    }
}
