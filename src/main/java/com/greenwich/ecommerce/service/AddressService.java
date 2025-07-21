package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.AddressRequestDTO;
import com.greenwich.ecommerce.dto.response.AddressResponseDTO;
import com.greenwich.ecommerce.entity.User;

import java.util.List;

public interface AddressService {

    AddressResponseDTO addAddress(Long userId, AddressRequestDTO addressRequestDTO);
    List<AddressResponseDTO> getAddresses(Long userId);

    boolean isAddressDuplicate(String addressLine);
    AddressResponseDTO updateAddress(Long userId, Long addressId, AddressRequestDTO addressRequestDTO);

    AddressResponseDTO setDefaultAddress(Long userId, Long addressId);
    AddressResponseDTO getDefaultAddress(Long userId);

}
