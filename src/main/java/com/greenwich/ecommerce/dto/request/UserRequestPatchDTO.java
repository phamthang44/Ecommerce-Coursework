package com.greenwich.ecommerce.dto.request;

import com.greenwich.ecommerce.common.enums.UserType;
import com.greenwich.ecommerce.dto.validator.EnumPattern;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class UserRequestPatchDTO {

    @EnumPattern(
            name = "role",
            message = "User type must be one of the following: CUSTOMER, ADMIN",
            regexp = "CUSTOMER|ADMIN"
    )
    @Schema(
            description = "User type, must be one of the following: CUSTOMER, ADMIN",
            example = "admin"
    )
    private UserType userType;

}
