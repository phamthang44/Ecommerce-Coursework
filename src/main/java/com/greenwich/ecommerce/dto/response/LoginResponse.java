package com.greenwich.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse extends ResponseData<Object> {
    private String accessToken;

    public LoginResponse(int status, String message, Object data) {
        super(status, message, data);
    }
    public LoginResponse(int status, String message, Object data, String accessToken) {
        super(status, message, data);
        this.accessToken = accessToken;
    }


}

