package com.greenwich.ecommerce.common.mapper;

import com.greenwich.ecommerce.common.enums.Gender;
import com.greenwich.ecommerce.dto.request.RegisterRequestDTO;
import com.greenwich.ecommerce.dto.response.UserDetailsResponse;
import com.greenwich.ecommerce.entity.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;


@Mapper(componentModel = "spring")
public interface UserMapper {
//    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", constant = "ACTIVE")
    @Mapping(target = "addresses", ignore = true)
    @Mapping(target = "dateOfBirth", ignore = true)
    User toUser(RegisterRequestDTO dto);

    @AfterMapping
    default void handleDefaults(@MappingTarget User user, RegisterRequestDTO dto) {
        if (user.getGender() == null) {
            user.setGender(Gender.OTHER);
        }
    }



}
