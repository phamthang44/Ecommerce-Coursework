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

    private Long id;
    private String addressLine;
    private Long customerId;
    private Double totalPrice;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private Double discount;
    private String status;
    private List<OrderItemResponseDTO> orderItems;


}
