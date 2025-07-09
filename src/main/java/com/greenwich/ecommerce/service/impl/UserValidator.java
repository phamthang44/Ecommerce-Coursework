package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.util.Util;
import com.greenwich.ecommerce.exception.DuplicateResourceException;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserValidator {

    private final UserRepository userRepository;

    public void validateEmail(String email) {
        if (Util.isNullOrEmpty(email)) {
            log.error("Email is null or empty");
            throw new InvalidDataException("Email is required");
        }
        if (Util.isInvalidEmailFormat(email)) {
            log.error("Invalid email format");
            throw new InvalidDataException("Invalid email format");
        }
        if (Util.isInvalidEmailLength(email)) {
            log.error("Invalid email length");
            throw new InvalidDataException("Email must not exceed 255 characters");
        }
        if (userRepository.existsByEmail(email)) {
            log.error("Email already exists");
            throw new DuplicateResourceException("Email is already registered");
        }
    }

    public void validatePassword(String password) {
        if (Util.isNullOrEmpty(password)) {
            log.error("Password is null or empty");
            throw new InvalidDataException("Password is required");
        }
        if (Util.isInvalidPasswordFormat(password)) {
            log.error("Invalid password format: {}", password);
            throw new InvalidDataException("Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character");
        }
    }

    public void validateUsername(String username) {
        if (Util.isNullOrEmpty(username)) {
            log.error("Username cannot be null or empty");
            throw new InvalidDataException("Username is required");
        }
        if (userRepository.existsByUsername(username)) {
            log.error("Username {} is already registered", username);
            throw new DuplicateResourceException("Username is already registered");
        }
    }

    public void validateFullName(String fullName) {
        if (Util.isNullOrEmpty(fullName)) {
            log.error("Full name cannot be null or empty");
            throw new InvalidDataException("Full name is required");
        }
        if (Util.isInvalidFullNameFormat(fullName)) {
            log.error("Invalid full name format: {}", fullName);
            throw new InvalidDataException("Full name must contain only letters, spaces, hyphens or apostrophes (2–50 chars)");
        }
    }

    public void validatePhoneNumber(String phoneNumber) {
        if (Util.isNullOrEmpty(phoneNumber)) {
            log.error("Phone number cannot be null or empty");
            throw new InvalidDataException("Phone number is required");
        }
        if (Util.isInvalidPhoneNumberFormat(phoneNumber)) {
            log.error("Invalid phone number format: {}", phoneNumber);
            throw new InvalidDataException("Invalid phone number format - must be 10 digits");
        }
    }

}
