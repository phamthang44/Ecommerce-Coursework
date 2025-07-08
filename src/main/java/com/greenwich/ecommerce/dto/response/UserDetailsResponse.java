package com.greenwich.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.greenwich.ecommerce.common.enums.UserStatus;
import com.greenwich.ecommerce.entity.Role;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsResponse implements Serializable {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String role;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserStatus status;

}
