package com.greenwich.ecommerce.dto.response;


import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private String productName;
    private String productDescription;
    private String unit;
    private BigDecimal price;
    private int quantity;
    private String stockStatus;

}
