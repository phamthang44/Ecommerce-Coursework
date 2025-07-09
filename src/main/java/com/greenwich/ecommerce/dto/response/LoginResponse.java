package com.greenwich.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

public class LoginResponse  extends ResponseData<Void> {

    private int status;
    private String message;
    private String accessToken;

    public LoginResponse(int status, String message, String accessToken) {
        super(status, message);
        this.status = status;
        this.message = message;
        this.accessToken = accessToken;
    }

}

