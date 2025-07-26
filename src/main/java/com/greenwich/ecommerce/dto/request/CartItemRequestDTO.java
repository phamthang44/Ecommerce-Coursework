package com.greenwich.ecommerce.dto.request;

import com.greenwich.ecommerce.entity.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CartItemRequestDTO implements Serializable {

    @NotNull(message = "Product ID is required")
    @Min(value = 1, message = "Product ID must be greater than or equal to 1")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
