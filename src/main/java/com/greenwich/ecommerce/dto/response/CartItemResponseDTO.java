package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDTO {

    private Long productId;
    private String name;
    private int quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

}
