package com.greenwich.ecommerce.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponseDTO implements Serializable {

    @NotBlank(message = "Address line cannot be blank")
    private String addressLine;

    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotBlank(message = "Postcode cannot be blank")
    private String postCode;

    @NotBlank(message = "Country cannot be blank")
    private String country;

    @NotNull(message = "Recipient name cannot be null")
    private boolean isDefault;

}
