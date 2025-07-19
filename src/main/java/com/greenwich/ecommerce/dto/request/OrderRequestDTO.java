package com.greenwich.ecommerce.dto.request;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class OrderRequestDTO implements java.io.Serializable{
    private List<OrderItemRequestDTO> items;
}
