package com.greenwich.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AddressRequestDTO {

    @NotBlank(message = "Address line cannot be blank")
    private String addressLine;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "Postcode cannot be blank")
    private String postCode;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    @NotNull(message = "Default status cannot be null")
    private boolean defaultAddress;

}
