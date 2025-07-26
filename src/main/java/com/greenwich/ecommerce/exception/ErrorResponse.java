package com.greenwich.ecommerce.exception;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private Date timestamp;  //thời gian xảy ra lỗi
    private int status;  //mã lỗi HTTP
    private String path;
    private String error;
    private String message;  //thông điệp lỗi
}
