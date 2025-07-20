package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreateResponse {

    private String status; // PENDING, SUCCESS, FAILED
    private LocalDateTime paymentTime;
    private String visaCheckRef; // nếu là VISA

}
