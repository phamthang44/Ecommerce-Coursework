package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {
//    private Long orderId; // map voi thang Response Order de biet order nao
    private Long id; // id cua thang order item, phan biet giua cac order item
    private Long productId; // dung de map qua product
    private String name;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subTotalPrice;
    private String assetUrl;
}
