package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.AddressRequestDTO;
import com.greenwich.ecommerce.dto.response.AddressResponseDTO;
import com.greenwich.ecommerce.entity.User;

import java.util.List;

public interface AddressService {

    void addAddress(AddressRequestDTO addressRequestDTO);
    List<AddressResponseDTO> getAddresses(Long userId);
}
