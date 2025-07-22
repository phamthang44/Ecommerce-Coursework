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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;
    private final AddressMapper addressMapper;

    @Transactional
    @Override
    public AddressResponseDTO addAddress(Long userId, AddressRequestDTO dto) {
        if (dto == null) {
            throw new InvalidDataException("Address cannot be null");
        }

        User user = userService.getUserById(userId);

        boolean isDuplicate = user.getAddresses().stream()
                .anyMatch(a -> a.getUserAddress().equalsIgnoreCase(dto.getAddressLine())
                        && a.getCity().equalsIgnoreCase(dto.getCity())
                        && a.getCountry().equalsIgnoreCase(dto.getCountry()));

        if (isDuplicate) {
            throw new InvalidDataException("Address already exists");
        }

        Address address = addressMapper.toAddressEntity(dto);
        log.info("Adding address for user with ID: {}", userId);
        log.info("Address line : {}", address.getUserAddress());
        address.setCreatedAt(LocalDateTime.now());
        address.setUpdatedAt(LocalDateTime.now());
        log.info("Address line 2: {}", address.getUserAddress());
        address.setDefault(false);
        user.saveAddress(address);
        addressRepository.save(address);
        return addressMapper.toAddressResponseDTO(address);
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

    @Override
    public boolean isAddressDuplicate(String addressLine) {
        return addressRepository.existsByUserAddress(addressLine);
    }

    @Transactional
    @Override
    public AddressResponseDTO updateAddress(Long userId, Long addressId, AddressRequestDTO addressRequestDTO) {

        log.info("Updating address for user with ID: {}", userId);
        log.info("Address ID: {}", addressId);

        //logic validate address
        if (addressRequestDTO == null) {
            throw new InvalidDataException("Address request cannot be null");
        }
        log.info("Address line new: {}", addressRequestDTO.getAddressLine());
        User user = userService.getUserById(userId);
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new InvalidDataException("Address not found with ID: " + addressId));
        if (!user.getAddresses().contains(address)) {
            throw new InvalidDataException("Address does not belong to the user");
        }
        log.info("Updating address old information: {}, {}", address.getUserAddress(), address.getCity());
        address.setUserAddress(addressRequestDTO.getAddressLine());
        address.setCity(addressRequestDTO.getCity());
        address.setPostalCode(addressRequestDTO.getPostCode());
        log.info("Updating address new information: {}, {}, {}", address.getUserAddress(), address.getCity(), address.getPostalCode());
        address.setCountry(addressRequestDTO.getCountry());
        address.setUpdatedAt(LocalDateTime.now());
        addressRepository.save(address);
        return addressMapper.toAddressResponseDTO(address);
    }

    @Transactional
    @Override
    public AddressResponseDTO setDefaultAddress(Long userId, Long addressId) {

        User user = userService.getUserById(userId);
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new InvalidDataException("Address not found with ID: " + addressId));

        if (!user.getAddresses().contains(address)) {
            throw new InvalidDataException("Address does not belong to the user");
        }

        // Set all addresses to non-default
        user.getAddresses().forEach(addr -> {
            addr.setDefault(false);
            addressRepository.save(addr);
        });

        // Set the specified address as default
        address.setDefault(true);

        log.info("Set address with ID: {} as default for user with ID: {}", addressId, userId);

        // Return the updated address
        return addressMapper.toAddressResponseDTO(addressRepository.save(address));
    }

    @Override
    public AddressResponseDTO getDefaultAddress(Long userId) {
        User user = userService.getUserById(userId);
        Address defaultAddress = user.getAddresses().stream()
                .filter(Address::isDefault)
                .findFirst()
                .orElseThrow(() -> new InvalidDataException("No default address found for user with ID: " + userId));

        return addressMapper.toAddressResponseDTO(defaultAddress);
    }
}
