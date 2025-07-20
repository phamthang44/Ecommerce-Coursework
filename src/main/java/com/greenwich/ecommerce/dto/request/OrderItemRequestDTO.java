package com.greenwich.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class OrderItemRequestDTO implements Serializable {

    @NotNull(message = "Product ID is required")
    @Min(value = 1, message = "Product ID must be greater than or equal to 1")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

}
