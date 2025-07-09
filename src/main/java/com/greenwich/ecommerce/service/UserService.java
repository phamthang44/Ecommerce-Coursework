package com.greenwich.ecommerce.service;


import com.greenwich.ecommerce.dto.request.RegisterRequestDTO;
import com.greenwich.ecommerce.dto.response.UserDetailsResponse;

public interface UserService {

    long registerUser(RegisterRequestDTO registerRequestDTO);
    UserDetailsResponse getUserDetailsByEmail(String email);
//    String findExistingUserByUserName(String username);
}
