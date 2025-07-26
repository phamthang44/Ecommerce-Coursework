package com.greenwich.ecommerce.dto.response;


import com.greenwich.ecommerce.common.enums.StockStatus;
import com.greenwich.ecommerce.common.enums.Unit;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO implements Serializable {

    private Long id;
    private String productName;
    private String productDescription;
    private String productType;
    private Unit unit;
    private BigDecimal price;
    private int quantity;
    private StockStatus stockStatus;
    private List<AssetResponse> assets;

}
