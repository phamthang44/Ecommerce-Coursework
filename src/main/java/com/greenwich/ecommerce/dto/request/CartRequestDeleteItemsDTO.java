package com.greenwich.ecommerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.util.List;

@Getter
public class CartRequestDeleteItemsDTO {

    @NotEmpty(message = "Cart item IDs cannot be empty")
    private List<Long> cartItemIds;

}
