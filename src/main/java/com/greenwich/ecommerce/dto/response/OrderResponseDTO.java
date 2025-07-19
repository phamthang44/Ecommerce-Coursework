package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {

    private Long id; // phan biet xem order nao cua 1 thang customer nao do
    private Long customerId; // phan biet xem
    private List<OrderItemResponseDTO> orderItems;

}
