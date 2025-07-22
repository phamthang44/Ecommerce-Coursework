package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptResponseDTO {
    private Long id;
    private Long orderId;
    private Long paymentId;
    private BigDecimal finalPrice;
    private String paymentStatus;
}
