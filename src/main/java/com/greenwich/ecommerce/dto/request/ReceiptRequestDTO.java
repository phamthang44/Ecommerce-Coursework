package com.greenwich.ecommerce.dto.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ReceiptRequestDTO implements Serializable {
    private Long userId;
    private Long paymentId;
}
