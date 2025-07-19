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

    private Long id;
    private String addressLine;
    private String city;
    private String postCode;
    private String country;
    private boolean defaultAddress;

}
