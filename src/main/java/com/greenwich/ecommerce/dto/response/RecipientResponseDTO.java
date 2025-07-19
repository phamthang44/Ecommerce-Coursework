package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipientResponseDTO implements Serializable {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String postCode;
    private String country;
    private boolean isDefault;

}
