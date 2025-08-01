package com.greenwich.ecommerce.dto.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDTO implements Serializable {

    private Long id;
    private Long productId;
    private String name;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subTotalPrice;
    private String assetUrl;
}
