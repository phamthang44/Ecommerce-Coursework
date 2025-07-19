package com.greenwich.ecommerce.dto.request;

import lombok.Getter;

@Getter
public class RecipientRequestDTO {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String postCode;
    private String country;
    private boolean isDefault;

}
