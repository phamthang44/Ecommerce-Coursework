package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptResponseDTO {

    private String orderCode;
    private String visaCheckRef;
    private LocalDateTime paymentDate;
    private BigDecimal finalPrice;
    private String paymentStatus;

}
