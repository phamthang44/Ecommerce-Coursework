package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.mapper.AddressMapper;
import com.greenwich.ecommerce.dto.request.AddressRequestDTO;
import com.greenwich.ecommerce.dto.response.AddressResponseDTO;
import com.greenwich.ecommerce.entity.Address;
import com.greenwich.ecommerce.entity.User;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.repository.AddressRepository;
import com.greenwich.ecommerce.service.AddressService;
import com.greenwich.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final AddressMapper addressMapper;
    @Override
    public void addAddress(AddressRequestDTO addressRequestDTO) {
        // Validate the user and addressRequestDTO
        if (addressRequestDTO == null) {
            throw new InvalidDataException("User and address details must not be null");
        }

        Address address = addressMapper.toAddressEntity(addressRequestDTO);
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());
        addressRepository.save(address);

    }

    @Override
    public List<AddressResponseDTO> getAddresses(Long userId) {
        // Validate the userId
        if (userId == null) {
            throw new InvalidDataException("User ID must not be null");
        }
        User user = userService.getUserById(userId);
        Set<Address> addresses = user.getAddresses();

        return addresses.stream().map(
                addressMapper::toAddressResponseDTO
        ).toList();
    }
}
