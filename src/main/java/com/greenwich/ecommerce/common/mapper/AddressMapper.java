package com.greenwich.ecommerce.common.mapper;

import com.greenwich.ecommerce.dto.request.AddressRequestDTO;
import com.greenwich.ecommerce.dto.response.AddressResponseDTO;
import com.greenwich.ecommerce.entity.Address;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address toAddressEntity(AddressRequestDTO request);

    AddressResponseDTO toAddressResponseDTO(Address address);
}
