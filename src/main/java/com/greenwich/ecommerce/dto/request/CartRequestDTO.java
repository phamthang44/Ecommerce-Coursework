package com.greenwich.ecommerce.dto.request;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class CartRequestDTO implements Serializable {
    private List<CartItemRequestDTO> items;
}
