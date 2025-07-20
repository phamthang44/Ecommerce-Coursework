package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long paymentId;
    private BigDecimal amount;
    private LocalDateTime paymentDate;
    private String status;
    private String orderCode;

}
