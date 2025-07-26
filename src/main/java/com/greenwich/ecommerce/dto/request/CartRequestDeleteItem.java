package com.greenwich.ecommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CartRequestDeleteItem {

    @NotNull(message = "Cart item ID is required")
    private Long cartItemId;

}
