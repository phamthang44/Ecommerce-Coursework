package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentProcessResponse {

    private String visaCheckReference;
    private String status;
    private LocalDateTime paymentTime;
    private String message;

}
