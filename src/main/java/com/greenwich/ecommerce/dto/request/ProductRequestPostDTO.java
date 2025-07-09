package com.greenwich.ecommerce.dto.request;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class ProductRequestPostDTO implements Serializable {

    private String productName;
    private BigDecimal price;
    private String category;
    private String description;
    private String unit;
    private int quantity;


}
