package com.greenwich.ecommerce.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class CartRequestDTO implements Serializable {

    @NotEmpty(message = "Cart items cannot be empty")
    private List<CartItemRequestDTO> items;

}
