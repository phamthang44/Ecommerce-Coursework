package com.greenwich.ecommerce.dto.request;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderRequestDTO implements Serializable{

    private Long addressId;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount; //da tru di discount

    private Double discount;

    private Double totalPrice;

    private List<OrderItemRequestDTO> items;
}
