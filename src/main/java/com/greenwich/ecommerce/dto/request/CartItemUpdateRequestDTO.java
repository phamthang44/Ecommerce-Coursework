package com.greenwich.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartItemUpdateRequestDTO {

    @NotNull(message = "CartItem ID is required")
    @Min(value = 1, message = "CartItem ID must be greater than or equal to 1")
    private Long cartItemId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
