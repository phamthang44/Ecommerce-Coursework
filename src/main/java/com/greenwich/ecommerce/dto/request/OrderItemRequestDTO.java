package com.greenwich.ecommerce.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class OrderItemRequestDTO implements Serializable {
    @NotNull(message = "Order item IDs cannot be empty")
    List<Long> cartItemIds;
}
