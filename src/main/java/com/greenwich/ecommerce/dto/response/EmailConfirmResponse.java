package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailConfirmResponse implements Serializable {

    private String message;
    private String token;



}
