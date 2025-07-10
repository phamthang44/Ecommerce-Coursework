package com.greenwich.ecommerce.dto.request;

import com.greenwich.ecommerce.common.enums.StockStatus;
import com.greenwich.ecommerce.common.enums.Unit;
import com.greenwich.ecommerce.dto.validator.EnumPattern;
import jakarta.validation.constraints.*;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class ProductRequestPostDTO implements Serializable {

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must have up to 2 decimal places")
    private BigDecimal price;

    @NotNull(message = "Category is required for the product")
    private Long categoryId;

    @NotBlank(message = "Product description is required")
    private String description;

    @NotNull(message = "Unit is required")
    @EnumPattern(name = "unit", regexp = "PIECE|GB|BOX|MONTH", message = "Unit must be one of the following: piece, gb, box, month")
    private Unit unit;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be a non-negative integer")
    private Integer quantity;

    @EnumPattern(name = "stockStatus", regexp = "IN_STOCK|OUT_OF_STOCK|LIMITED|PRE_ORDER", message = "Stock status must be one of the following: in_stock, out_of_stock, limited, pre_order")
    @NotNull(message = "Stock status is required")
    private StockStatus stockStatus;

}
