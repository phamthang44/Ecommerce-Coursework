package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

//    private Long id; // phan biet xem order nao cua 1 thang customer nao do
//    private Long customerId; // phan biet xem
//    private BigDecimal totalPrice;
//    private List<OrderItemResponseDTO> orderItems;
    //------ Thanh -------------

    private String orderCode;
    private String customerName;
    private String addressLine;
    private BigDecimal totalPrice;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private BigDecimal discountApplied;
    private String status;
    private List<OrderItemResponseDTO> orderItems;


}
