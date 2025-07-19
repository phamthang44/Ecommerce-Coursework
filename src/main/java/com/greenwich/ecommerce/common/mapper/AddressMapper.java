package com.greenwich.ecommerce.common.mapper;

import com.greenwich.ecommerce.dto.request.AddressRequestDTO;
import com.greenwich.ecommerce.dto.response.AddressResponseDTO;
import com.greenwich.ecommerce.entity.Address;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "userAddress", source = "addressLine")
    @Mapping(target = "postalCode", source = "postCode")
    @Mapping(target = "isDefault", source = "defaultAddress")
    Address toAddressEntity(AddressRequestDTO request);

    @Mapping(target = "addressLine", source = "userAddress")
    @Mapping(target = "postCode", source = "postalCode")
    @Mapping(target = "defaultAddress", source = "default")
    @Mapping(target = "id", source = "id")
    AddressResponseDTO toAddressResponseDTO(Address address);
}
