package com.greenwich.ecommerce.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@Builder
public class ResponseData<T> {

    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResponseData(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseData(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

}
