package com.greenwich.ecommerce.dto.request;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class CreatePaymentInput implements Serializable {
    private Long orderId;
    private BigDecimal totalAmount;
}
