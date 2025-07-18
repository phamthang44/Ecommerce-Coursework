package com.greenwich.ecommerce.service;


import com.greenwich.ecommerce.common.enums.UserType;
import com.greenwich.ecommerce.dto.request.RegisterRequestDTO;
import com.greenwich.ecommerce.dto.response.UserDetailsResponse;
import com.greenwich.ecommerce.entity.User;
import jakarta.mail.MessagingException;

public interface UserService {

    long registerUser(RegisterRequestDTO registerRequestDTO) throws MessagingException;
    UserDetailsResponse getUserDetailsByEmail(String email);
//    String findExistingUserByUserName(String username);
    UserDetailsResponse getCurrentUser(Long userId);
    UserDetailsResponse updateUserRole(Long userId, UserType userType);
    User getUserById(Long userId);
}
