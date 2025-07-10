package com.greenwich.ecommerce.dto.response;


import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO implements Serializable {

    private String productName;
    private String productDescription;
    private String unit;
    private BigDecimal price;
    private int quantity;
    private String stockStatus;

}
