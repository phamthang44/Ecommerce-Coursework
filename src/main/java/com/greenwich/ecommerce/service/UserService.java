package com.greenwich.ecommerce.service;


import com.greenwich.ecommerce.dto.request.RegisterRequestDTO;

public interface UserService {

    long registerUser(RegisterRequestDTO registerRequestDTO);


}
